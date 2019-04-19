package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beans.LocalisationBean;
import beans.ParentChildBean;
import beans.ProgrammeBean;
import beans.ProjetBean;
import dao.GenericDao;
import dao.ProjetDao;
import dto.PartnerDto;
import dto.ProjetEditDto;
import dto.SelectGrpDto;
import dto.SelectGrpsItemDto;
import dto.SimpleDto;
import dto.TreeDto;
import dto.TreePathDto;
import dto.TreeProgrammeDto;
import entities.Acheteur;
import entities.Commune;
import entities.Fraction;
import entities.IndhProgramme;
import entities.Localisation;
import entities.Projet;
import entities.ProjetMaitreOuvrage;
import entities.ProjetPartenaire;
import entities.Secteur;
import entities.SrcFinancement;

@Service
public class ProjetService {
	
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private ProjetDao projetDao;
	@Autowired
	private GenericDao<Projet, Integer> genericProjetDao;
	@Autowired
	private GenericDao<ProjetMaitreOuvrage, Integer> gProjetMaitreOuvrageDao;
	
	
	
	@Transactional(rollbackOn = Exception.class)
	public Integer saveProjet(ProjetBean bean) {
		
		Projet projet = bean.idProjet != null ? genericProjetDao.read(bean.idProjet, Projet.class) : new Projet();
		
		projet.setIntitule(bean.intitule);
		projet.setMontant(bean.montant);
		projet.setConvention(bean.isConvention);
		projet.setSecteur(new Secteur(bean.secteur));
		projet.setDateSaisie(new Date());
		

		
		///////// Localisations
		projet.getLocalisations().clear();
		bean.localisations.forEach( loc -> {
			
			List<Integer> t = Arrays.stream(loc.split("\\.")).map(Integer::parseInt).collect(Collectors.toList());
			
			projet.getLocalisations().add(
					new Localisation( 
							projet,
							new Commune(t.get(0)), 
							t.size() > 1 ? new Fraction(t.get(1)) : null
					));
		});
		
		
		///////// Partenaires
		projet.getProjetPartenaires().clear();
		if(bean.isConvention) {
			bean.partners.forEach( p -> {
				String[] t = p.split("\\:");

				projet.getProjetPartenaires().add(
						new ProjetPartenaire(
									projet, 
									new Acheteur(Integer.parseInt(t[0])), 
									Double.parseDouble(t[1]),
									t.length == 3 ? new SrcFinancement(Integer.parseInt(t[2])) : null
								)
						);
			});
		}
		
		if(bean.idProjet == null) {
			genericProjetDao.create(projet);
		}
		
		///////// Maitre ouvrage
		String[] t = bean.maitreOuvrage.split("\\:");
		projet.setProjetMaitreOuvrage(gProjetMaitreOuvrageDao.create(new ProjetMaitreOuvrage(
				new Acheteur(Integer.parseInt(t[0])), 
				projet, 
				t.length == 2 ? new SrcFinancement(Integer.parseInt(t[1])) : null,
				false
		)));
		projet.setProjetMaitreOuvrageDelegue(
				bean.isMaitreOuvrageDel ? gProjetMaitreOuvrageDao.create(new ProjetMaitreOuvrage(new Acheteur(bean.maitreOuvrageDel), projet, true)) : null
		);
		

		
//		entityManager.flush();

		return projet.getId();
	}
	
	
	public ProjetEditDto getProjetForEdit(Integer idProjet) {
		
		Projet projet = projetDao.getProjetForEdit(idProjet);
		
		Integer xx = projet.getProjetMaitreOuvrage().getSrcFinancement() != null ? projet.getProjetMaitreOuvrage().getSrcFinancement().getId() : null;
		
		ProjetEditDto dto = new ProjetEditDto(
			projet.getIntitule(), projet.getMontant(), projet.isConvention(), 
			new SimpleDto(projet.getProjetMaitreOuvrage().getMaitreOuvrage().getId(), projet.getProjetMaitreOuvrage().getMaitreOuvrage().getNom()) , 
			projet.getProjetMaitreOuvrage().getSrcFinancement() != null ? projet.getProjetMaitreOuvrage().getSrcFinancement().getId() : null,
			projet.getProjetMaitreOuvrageDelegue() != null ? true:false,
			projet.getProjetMaitreOuvrageDelegue() != null ? 
					new SimpleDto(projet.getProjetMaitreOuvrageDelegue().getMaitreOuvrage().getId(), projet.getProjetMaitreOuvrageDelegue().getMaitreOuvrage().getNom()) : null,
			projet.getSecteur().getId()		
		);
		
		projet.getProjetPartenaires().forEach(pp -> {
			dto.partners.add(new PartnerDto(
					new SimpleDto(pp.getPartenaire().getId(), pp.getPartenaire().getNom()), pp.getFinancement(), 
					pp.getSrcFinancement() != null ? pp.getSrcFinancement().getId() : null
					)
			);
		});
		
		projet.getLocalisations().forEach(loc -> {
			dto.localisations.add(loc.getCommune().getId()+""+(loc.getFraction() != null ? "."+loc.getFraction().getId(): ""));
		});
		
		return dto;
	} 
	
	public Collection<TreePathDto> getCommunesWithFractions() {
		
		List<LocalisationBean> communes = projetDao.getCommunesWithFractions();
		
		Map<Integer, TreePathDto> communetree = new LinkedHashMap<Integer, TreePathDto>();
		communes.forEach((com) -> {
            if (!communetree.containsKey(com.idCommune)){
            	communetree.put(com.idCommune, new TreePathDto(com.idCommune, com.commune, String.valueOf(com.idCommune)));
            }
            communetree.get(com.idCommune).children.add(new TreePathDto(com.idFraction, com.fraction, com.idCommune+"."+com.idFraction));
		});
		
		return communetree.values();
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<SelectGrpDto> getProgrammesWithPhases2() {
		
		List<ProgrammeBean> programmes = projetDao.getIndhProgrammes2();
		Map<Integer, List<SimpleDto>> mapProgWithLeafs = new LinkedHashMap<Integer, List<SimpleDto>>();
		Map<Integer, SelectGrpDto> selectGrpMapPhase = new LinkedHashMap<Integer, SelectGrpDto>();
	
		programmes.forEach((prog) -> {
			
			if(prog.idParent != null) {
				if(!mapProgWithLeafs.containsKey(prog.idParent))
					mapProgWithLeafs.put(prog.idParent, new ArrayList<SimpleDto>());
				mapProgWithLeafs.get(prog.idParent).add(new SimpleDto(prog.id, prog.label));
			} 

		});
		
		
		programmes.forEach((prog) -> {
			
			if( prog.idParent == null ) {
				
				if (!selectGrpMapPhase.containsKey(prog.phase)){
					selectGrpMapPhase.put(prog.phase, new SelectGrpDto("Phase " + 
							String.join("", Collections.nCopies(prog.phase, "I"))));
				}
				
				if(!mapProgWithLeafs.containsKey(prog.id)) {
					selectGrpMapPhase.get(prog.phase).addOption(new SimpleDto(prog.id, prog.label));
				} else {
					selectGrpMapPhase.get(prog.phase).addOption(new SelectGrpDto(prog.label, mapProgWithLeafs.get(prog.id)));
				}
				
			}

	
			
		});
	

		
		return selectGrpMapPhase.values();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<SelectGrpDto> getParentProgrammesWithPhases() {
		
		List<ProgrammeBean> programmes = projetDao.getParentProgrammes();

		Map<Integer, SelectGrpDto> selectGrpMapPhase = new LinkedHashMap<Integer, SelectGrpDto>();

		programmes.forEach((prog) -> {

			if (!selectGrpMapPhase.containsKey(prog.phase)){
				selectGrpMapPhase.put(prog.phase, new SelectGrpDto("Phase " + 
						String.join("", Collections.nCopies(prog.phase, "I"))));
			}
			
			selectGrpMapPhase.get(prog.phase).options.add(new SimpleDto(prog.id, prog.label));

		});
		
		
		
		return selectGrpMapPhase.values();
	}

	

}
