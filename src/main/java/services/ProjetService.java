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
import dao.SearchProjetDao;
import dto.PartnerDto;
import dto.ProjetBasicDto;
import dto.ProjetDto;
import dto.ProjetEditDto;
import dto.SelectGrpDto;
import dto.SelectGrpsItemDto;
import dto.SimpleDto;
import dto.TreeDto;
import dto.TreePathDto;
import dto.TreeProgrammeDto;
import dto.UserSession;
import entities.Acheteur;
import entities.Commune;
import entities.Fraction;
import entities.IndhProgramme;
import entities.Localisation;
import entities.Marches;
import entities.Projet;
import entities.ProjetIndh;
import entities.ProjetMaitreOuvrage;
import entities.ProjetPartenaire;
import entities.Secteur;
import entities.SrcFinancement;
import entities.User;
import enums.ContributionEnum;

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
	public Integer saveProjet(ProjetBean bean, UserSession userSession) {
		
		Projet projet = bean.idProjet != null ? genericProjetDao.find(bean.idProjet, Projet.class) : new Projet();
		
		projet.setIntitule(bean.intitule);
		projet.setMontant(bean.montant);
		projet.setConvention(bean.isConvention);
		projet.setSecteur(new Secteur(bean.secteur));
		projet.setSrcFinancement(new SrcFinancement(bean.srcFinancement));
		projet.setAnneeProjet(bean.anneeProjet);
		projet.setChargeSuivi(new User(bean.chargeSuivi != null ? bean.chargeSuivi : userSession.id));
		
//		projet.setPrdts(bean.prdts);

		
		if(bean.idProjet == null) {
			projet.setDateSaisie(new Date());
			projet.setUserSaisie(new User(userSession.id));
			genericProjetDao.persist(projet);
		} else {
			projet.setDateLastModif(new Date());
		}

		projet.setIndh(null);
		projet.getProjetPartenaires().clear();
		projet.getLocalisations().clear();
		entityManager.flush();
		
		projet.setIndh(
				bean.srcFinancement.equals(enums.SrcFinancement.INDH.val()) ? 
						new ProjetIndh(projet, new IndhProgramme(bean.indhProgramme)) : null
		);

		
		if( bean.localisations != null && !bean.localisations.isEmpty() ) {
			
			bean.localisations.forEach( loc -> {
				
				List<Integer> t = Arrays.stream(loc.split("\\.")).map(Integer::parseInt).collect(Collectors.toList());
				
				projet.getLocalisations().add(
						new Localisation( 
								projet,
								new Commune(t.get(0)), 
								t.size() > 1 ? new Fraction(t.get(1)) : null
								));
			});
			
		}
		
		
		/////////////////////////////////////////////////// Partenaires
		
		if(bean.isConvention) {
			bean.partners.forEach( p -> {


				projet.getProjetPartenaires().add(
						new ProjetPartenaire(
									projet, 
									new Acheteur(p.partner.value), 
									p.montant,
									p.commentaire
								)
						);
			});
		}
		

		/////////////////////////////////////////////////// Maitre ouvrage
		

		projet.setProjetMaitreOuvrage(gProjetMaitreOuvrageDao.persist(new ProjetMaitreOuvrage(
				new Acheteur(bean.maitreOuvrage), 
				projet, 
				false
		)));
		projet.setProjetMaitreOuvrageDelegue(
				bean.isMaitreOuvrageDel ? gProjetMaitreOuvrageDao.persist(new ProjetMaitreOuvrage(new Acheteur(bean.maitreOuvrageDel), projet, true)) : null
		);


		return projet.getId();
	}
	
	
	public ProjetEditDto getProjetForEdit(Integer idProjet) {
		
		Projet projet = projetDao.getProjetForEdit(idProjet);
		
		ProjetEditDto dto = new ProjetEditDto(
			projet.getIntitule(), projet.getMontant(), projet.getSrcFinancement(), projet.isConvention(), 
			projet.getProjetMaitreOuvrage(),
			projet.getProjetMaitreOuvrageDelegue(),
			projet.getSecteur().getId(), projet.getChargeSuivi(), projet.getAnneeProjet(),
			projet.getIndh()
		);
		
		projet.getProjetPartenaires().forEach(pp -> {
			dto.partners.add( new PartnerDto( 
					new SimpleDto(pp.getPartenaire().getId(), pp.getPartenaire().getNom()), 
					pp.getFinancement(), pp.getFinancement() != null ? ContributionEnum.financiere.value : ContributionEnum.autres.value ,
					pp.getCommentaire()		
			));
		});
		
		projet.getLocalisations().forEach(loc -> {
			dto.localisations.add(loc.getCommune().getId()+""+(loc.getFraction() != null ? "."+loc.getFraction().getId(): ""));
		});
		
		return dto;
	} 
	
	public ProjetBasicDto getProjetForDetail(Integer idProjet) {
		
		Projet projet = projetDao.getProjetForEdit(idProjet);
		
		ProjetMaitreOuvrage pMod = projet.getProjetMaitreOuvrageDelegue();
		ProjetMaitreOuvrage pMo = projet.getProjetMaitreOuvrage();
		Acheteur mod = pMod != null ? pMod.getMaitreOuvrage() : null;
		Acheteur mo = pMo.getMaitreOuvrage();
		SimpleDto modDto = pMod != null ? new SimpleDto(mod.getId(), mod.getNom()) : null;

		
		ProjetBasicDto dto = new ProjetBasicDto(
				projet.getId(),
				projet.getIntitule(), projet.getMontant(), projet.isConvention(), projet.getAnneeProjet(), 
				modDto != null, new SimpleDto(mo.getId(), mo.getNom()), modDto,
				projet.getIndh() != null
			);
		
		
		Marches defaultMarche = projet.getDefaultMarche();
		dto.taux = 0;
		if( defaultMarche != null ) {
			
			if( defaultMarche.getDateReceptionProvisoire() != null ) {
				dto.taux = 100;
			}		
			else if( defaultMarche.getCurrentTaux() != null ) {			
				dto.taux = projet.getDefaultMarche().getCurrentTaux().getTaux();
			}
		}
		


		projet.getProjetPartenaires().forEach(pp -> {
			dto.partners.add( new PartnerDto( 
					new SimpleDto(pp.getPartenaire().getId(), pp.getPartenaire().getNom()), 
					pp.getFinancement(), pp.getFinancement() != null ? ContributionEnum.financiere.value : ContributionEnum.autres.value ,
					pp.getCommentaire()		
			));
		});
		
		if(projet.getIndh() != null) {			
			dto.indhProgramme = new SimpleDto(projet.getIndh().getProgramme().getId(), projet.getIndh().getProgramme().getLabel()) ;
		}
		
		dto.secteur = new SimpleDto(projet.getSecteur().getId(), projet.getSecteur().getNom()) ;
		dto.srcFinancement = (projet.getSrcFinancement() != null) ? 
				new SimpleDto(projet.getSrcFinancement() .getId(), projet.getSrcFinancement() .getLabel()) : null ;
		
		dto.chargeSuivi = (projet.getChargeSuivi() != null) ? 
				new SimpleDto(projet.getChargeSuivi() .getId(), projet.getChargeSuivi().getPrenom()+" "+projet.getChargeSuivi().getNom()) : null ;
		
		Map<Integer, TreeDto> locMap = new LinkedHashMap<>();
		projet.getLocalisations().forEach(loc -> {
			Commune com = loc.getCommune();
			if( !locMap.containsKey(com.getId()) ) {
				locMap.put(com.getId(), new TreeDto(com.getId(), com.getNom()));
			}
			if(loc.getFraction() != null) {				
				locMap.get(com.getId()).children.add(new TreeDto(loc.getFraction().getId(), loc.getFraction().getNom()));
			}
		});
		dto.localisations = locMap.values();
		
		
		return dto;
	} 
	
	



	

	

	

}
