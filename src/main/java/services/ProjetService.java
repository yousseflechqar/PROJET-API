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
import beans.ProjetSearchBean;
import dao.GenericDao;
import dao.ProjetDao;
import dto.PartnerDto;
import dto.ProjetDto;
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
import entities.ProjetIndh;
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
		
		projet.setPrdts(bean.prdts);
		
		if(bean.idProjet == null) {
			genericProjetDao.create(projet);
		}
		
		

		projet.setIndh(null);
		projet.getProjetPartenaires().clear();
		projet.getLocalisations().clear();
		entityManager.flush();
		
		///////// Localisations

		projet.setIndh(bean.indh ? new ProjetIndh(projet, new IndhProgramme(bean.indhProgramme)) : null);
		
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
	
		ProjetMaitreOuvrage pMod = projet.getProjetMaitreOuvrageDelegue();
		Acheteur mod = pMod != null ? pMod.getMaitreOuvrage() : null;
		Acheteur mo = projet.getProjetMaitreOuvrage().getMaitreOuvrage();
		SimpleDto modDto = pMod != null ? new SimpleDto(mod.getId(), mod.getNom()) : null;
		SrcFinancement moSrcFin = projet.getProjetMaitreOuvrage().getSrcFinancement();
		
		ProjetEditDto dto = new ProjetEditDto(
			projet.getIntitule(), projet.getMontant(), projet.isConvention(), 
			new SimpleDto(mo.getId(), mo.getNom()) , moSrcFin != null ? moSrcFin.getId() : null,
			modDto != null, modDto,
			projet.getSecteur().getId(),
			projet.getIndh() != null, projet.getIndh() != null ? projet.getIndh().getProgramme().getId():null, projet.isPrdts()
		);
		
		projet.getProjetPartenaires().forEach(pp -> {
			dto.partners.add(new PartnerDto(
					new SimpleDto(pp.getPartenaire().getId(), pp.getPartenaire().getNom()), pp.getFinancement(), 
					pp.getSrcFinancement() != null ? new SimpleDto(pp.getSrcFinancement().getId(), pp.getSrcFinancement().getLabel()) : null
					)
			);
		});
		
		projet.getLocalisations().forEach(loc -> {
			dto.localisations.add(loc.getCommune().getId()+""+(loc.getFraction() != null ? "."+loc.getFraction().getId(): ""));
		});
		
		return dto;
	} 
	


	
	public Collection<ProjetDto> getListProjets(){
		
		List<ProjetDto> projets = projetDao.getListProjets();
		
		Map<Integer, ProjetDto> projetsMap = new LinkedHashMap<Integer, ProjetDto>();
		
		projets.forEach((proj) -> {

			if (!projetsMap.containsKey(proj.id)){
				projetsMap.put(proj.id, proj);
			}
			
			projetsMap.get(proj.id).localisations.add(proj.communeLabel);

		});
		
		return projetsMap.values();
	}
	
	public Collection<ProjetDto> getListProjets2(ProjetSearchBean bean){
		
		List<ProjetDto> projets = projetDao.getListProjets2(bean);
		
		return projets;
	}
	

}
