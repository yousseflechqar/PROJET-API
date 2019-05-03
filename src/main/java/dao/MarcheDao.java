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
	
	
	public Marches getMarcheForEdit(Integer idMarche){
		
		try {
			return entityManager.createQuery(""
					+ "SELECT m FROM Marches m "

						+ "LEFT JOIN FETCH m.marchesSocietes mSte "
							+ "LEFT JOIN FETCH mSte.societe ste "
						+ "LEFT JOIN FETCH m.marchesTaux  "
						+ "LEFT JOIN FETCH m.MarchesOss  "
						+ "LEFT JOIN FETCH m.marchesDecomptes  "

					+ "WHERE m.id = :idMarche "
					+ "", Marches.class)
					.setParameter("idMarche", idMarche)
					.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	
	
}
