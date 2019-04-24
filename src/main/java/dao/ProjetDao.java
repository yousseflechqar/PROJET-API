package dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import beans.LocalisationBean;
import beans.ParentChildBean;
import beans.ProgrammeBean;
import beans.ProjetSearchBean;
import dto.ProjetDto;
import dto.SimpleDto;
import entities.Acheteur;
import entities.Commune;
import entities.IndhProgramme;
import entities.Localisation;
import entities.Projet;
import entities.ProjetMaitreOuvrage;



@Repository
public class ProjetDao {
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	

	public Projet getProjetForEdit(Integer idProjet){
		
		try {
			return (Projet) entityManager.createQuery(""
					+ "SELECT p FROM Projet p "
						+ "LEFT JOIN FETCH p.projetMaitreOuvrage pmo "
							+ "LEFT JOIN FETCH pmo.maitreOuvrage "
						+ "LEFT JOIN FETCH p.projetMaitreOuvrageDelegue pmo_ "
							+ "LEFT JOIN FETCH pmo_.maitreOuvrage "
						+ "LEFT JOIN FETCH p.localisations loc "
						+ "LEFT JOIN FETCH p.projetPartenaires pp "
							+ "LEFT JOIN FETCH pp.partenaire "
					+ "WHERE p.id = :idProjet"
					)
					.setParameter("idProjet", idProjet)
					.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProjetDto> getListProjets(){
		
		return entityManager.createQuery("SELECT new dto.ProjetDto(p.id, p.intitule, p.taux, mo.nom, com.id, com.nom) "
				+ "FROM Projet p "
					+ "LEFT JOIN p.projetMaitreOuvrage pmo "
					+ "LEFT JOIN pmo.maitreOuvrage mo "
						+ "LEFT JOIN pmo.maitreOuvrage mo "
					+ "LEFT JOIN p.localisations loc "
						+ "LEFT JOIN loc.commune com "
					+ "")
				.getResultList();
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<ProjetDto> getListProjets2(ProjetSearchBean bean){
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery cr = cb.createQuery(ProjetDto.class);
		Root projet = cr.from(Projet.class);
		

		Join<Projet, ProjetMaitreOuvrage> pmo = projet.join("projetMaitreOuvrage", JoinType.INNER);
		Join<ProjetMaitreOuvrage, Acheteur> mo = pmo.join("maitreOuvrage", JoinType.INNER);
		Join<Projet, Localisation> loc = projet.join("localisations", JoinType.INNER);
		Join<Localisation, Commune> com = loc.join("commune", JoinType.INNER);
		
		cr.select(
				cb.construct(
					ProjetDto.class,
					projet.get("id"),
					projet.get("intitule"),
					projet.get("taux"),
					mo.get("nom"),
					com.get("id"),
					com.get("nom")
			    )
		);

		Predicate sPredic = null;
		
		if(bean.intitule != null) {
			sPredic = cb.like(projet.get("intitule"), "%"+bean.intitule+"%");
		}

		if(bean.secteur != null) {
			sPredic = cb.equal(projet.get("secteur"), bean.secteur);
		}
		
		if(bean.maitreOuvrage != null) {
			sPredic = cb.equal(pmo.get("maitreOuvrage"), bean.maitreOuvrage);
		}
		
		cr.where(sPredic)
		;
		 

		
		List<ProjetDto> results = entityManager.createQuery(cr).getResultList();


		return results;
	}
	
	


	
	@SuppressWarnings("unchecked")
	public List<LocalisationBean> getCommunesWithFractions(){
		
		return entityManager.createQuery(" "
				
				+ " SELECT new beans.LocalisationBean(c.id, c.nom, f.id, f.nom) "
					+ " FROM Commune c "
					+ " LEFT JOIN c.fractions f ")
				
				.getResultList() ;
		
	}

	

}
