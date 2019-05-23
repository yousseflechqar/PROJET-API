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
			return entityManager.createQuery(""
					+ "SELECT p FROM Projet p "
					
						+ "LEFT JOIN FETCH p.secteur "
						+ "LEFT JOIN FETCH p.srcFinancement "
						
						+ "LEFT JOIN FETCH p.indh _in "
							+ "LEFT JOIN FETCH _in.programme "
						
						+ "LEFT JOIN FETCH p.projetMaitreOuvrage pmo "
							+ "LEFT JOIN FETCH pmo.maitreOuvrage "
							
						+ "LEFT JOIN FETCH p.projetMaitreOuvrageDelegue pmo_ "
							+ "LEFT JOIN FETCH pmo_.maitreOuvrage "
						
						+ "LEFT JOIN FETCH p.localisations loc "
							+ "LEFT JOIN FETCH loc.commune "
							+ "LEFT JOIN FETCH loc.fraction "
							
						+ "LEFT JOIN FETCH p.projetPartenaires pp "
							+ "LEFT JOIN FETCH pp.partenaire "
						
					+ "WHERE p.id = :idProjet "
					
					+ "", Projet.class)
					.setParameter("idProjet", idProjet)
					.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	

	

	
	




	

}
