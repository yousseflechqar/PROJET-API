package services;

import java.util.Arrays;
import java.util.Collection;
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
import beans.ProjetBean;
import dao.GenericDao;
import dao.ProjetDao;
import dto.PartnerDto;
import dto.ProjetEditDto;
import dto.SimpleDto;
import dto.TreeDto;
import entities.Commune;
import entities.Fraction;
import entities.Localisation;
import entities.MaitreOuvrage;
import entities.Partenaire;
import entities.Projet;
import entities.ProjetPartenaire;
import entities.Secteur;

@Service
public class ProjetService {
	
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private ProjetDao projetDao;
	@Autowired
	private GenericDao<Projet, Integer> genericProjetDao;

	
	
	@Transactional(rollbackOn = Exception.class)
	public Integer saveProjet(ProjetBean bean) {
		
		Projet projet = bean.idProjet != null ? genericProjetDao.read(bean.idProjet, Projet.class) : new Projet();
		
		projet.setIntitule(bean.intitule);
		projet.setMontant(bean.montant);

		
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
		
//		entityManager.detach(projet.getProjetPartenaires());
		projet.getProjetPartenaires().clear();
		if(bean.isConvention) {
			bean.partners.forEach( p -> {
				String[] t = p.split("\\:");
				
//				if(  projet.getProjetPartenaires().stream().anyMatch(pp -> pp.getPartenaire().getId().equals(Integer.parseInt(t[0]))) )
				projet.getProjetPartenaires().add(
						new ProjetPartenaire(
								projet, 
								new Partenaire(Integer.parseInt(t[0])), 
								Double.parseDouble(t[1])
								)
						);
			});
		}
		
		
		///////// Maitre ouvrage
		
		projet.setMaitreOuvrage(new MaitreOuvrage(bean.maitreOuvrage));
		projet.setMaitreOuvrageDelegue(bean.isMaitreOuvrageDel ? new MaitreOuvrage(bean.maitreOuvrageDel) : null);

		
		
		///
		
		projet.setSecteur(new Secteur(bean.secteur));
		
		projet.setDateSaisie(new Date());
		
		if(bean.idProjet == null) {
			genericProjetDao.create(projet);
		} else {
//			genericProjetDao.update(projet);
		}
		
		
		
		return projet.getId();
		
	}
	
	
	public ProjetEditDto getProjetForEdit(Integer idProjet) {
		
		Projet projet = projetDao.getProjetForEdit(idProjet);
		
		ProjetEditDto dto = new ProjetEditDto(
			projet.getIntitule(), projet.getMontant(), projet.getProjetPartenaires().size() > 0 ? true:false, 
			new SimpleDto(projet.getMaitreOuvrage().getId(), projet.getMaitreOuvrage().getNom()) , 
			projet.getMaitreOuvrageDelegue() != null ? true:false,
			projet.getMaitreOuvrageDelegue() != null ? 
					new SimpleDto(projet.getMaitreOuvrageDelegue().getId(), projet.getMaitreOuvrageDelegue().getNom()) : null,
			projet.getSecteur().getId()		
		);
		
		projet.getProjetPartenaires().forEach(pp -> {
			dto.partners.add(new PartnerDto(new SimpleDto(pp.getPartenaire().getId(), pp.getPartenaire().getNom()), pp.getFinancement()));
		});
		
		projet.getLocalisations().forEach(loc -> {
			dto.localisations.add(loc.getCommune().getId()+""+(loc.getFraction() != null ? "."+loc.getFraction().getId(): ""));
		});
		
		return dto;
	} 
	
	public Collection<TreeDto> getCommunesWithFractions() {
		
		List<LocalisationBean> communes = projetDao.getCommunesWithFractions();
		
		Map<Integer, TreeDto> communetree = new LinkedHashMap<Integer, TreeDto>();
		communes.forEach((com) -> {
            if (!communetree.containsKey(com.idCommune)){
            	communetree.put(com.idCommune, new TreeDto(com.idCommune, com.commune));
            }
            communetree.get(com.idCommune).children.add(new TreeDto(com.idFraction, com.fraction));
		});
		
		return communetree.values();
	}

}
