package dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import entities.Marches;
import entities.Projet;

@Repository
public class MarcheDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	
	public Marches getProjetForEdit(Integer idProjet){
		
		try {
			return entityManager.createQuery(""
//					+ "SELECT p FROM Projet p "
//						+ "LEFT JOIN FETCH p.projetMaitreOuvrage pmo "
//							+ "LEFT JOIN FETCH pmo.maitreOuvrage "
//						+ "LEFT JOIN FETCH p.projetMaitreOuvrageDelegue pmo_ "
//							+ "LEFT JOIN FETCH pmo_.maitreOuvrage "
//						+ "LEFT JOIN FETCH p.localisations loc "
//						+ "LEFT JOIN FETCH p.projetPartenaires pp "
//							+ "LEFT JOIN FETCH pp.partenaire "
//					+ "WHERE p.id = :idProjet"
					, Marches.class)
					.setParameter("idProjet", idProjet)
					.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	
	
}
