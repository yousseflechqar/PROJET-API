package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import dto.SimpleDto;
import entities.Marches;
import entities.Projet;
import enums.MarcheTypeEnum;

@Repository
public class MarcheDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	
	public Marches getMarcheForEdit(Integer idMarche){
		
		try {
			return entityManager.createQuery(""
					+ "SELECT m FROM Marches m "

						+ "LEFT JOIN FETCH m.marchesType "
						+ "LEFT JOIN FETCH m.marchesEtat "
						+ "LEFT JOIN FETCH m.marchesSocietes mSte "
							+ "LEFT JOIN FETCH mSte.societe ste "
						+ "LEFT JOIN FETCH m.marchesTaux  "
						+ "LEFT JOIN FETCH m.MarchesOss mOss "
							+ "LEFT JOIN FETCH mOss.osType "
						+ "LEFT JOIN FETCH m.marchesDecomptes "

					+ "WHERE m.id = :idMarche "
					+ "", Marches.class)
					.setParameter("idMarche", idMarche)
					.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}


	public Marches getDefaultMarche(Integer idProjet) {

		entityManager.createQuery(""
				+ "SELECT m.id from Marches m "
					+ "WHERE m.projet.id = :idProjet"
				+ "")
				.setParameter("idProjet", idProjet)
		;
		return null;
	}
	
	public Integer getTravauxMarcheId(Integer idProjet) {
		try {
				return 	
				entityManager.createQuery(""
					+ "SELECT m.id from Marches m "
						+ "WHERE m.projet.id = :idProjet AND m.marchesType.id = :mType "
						+ "ORDER BY m.id desc "
					+ "", Integer.class)
				.setParameter("idProjet", idProjet)
				.setParameter("mType", MarcheTypeEnum.travaux.value)
				.setMaxResults(1)
				.getSingleResult();
		
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Integer> getMarchesIdsByProjet(Integer idProjet) {
		return 	entityManager.createQuery("SELECT m.id from Marches m WHERE m.projet.id = :idProjet", Integer.class)
					.setParameter("idProjet", idProjet)
					.getResultList()
				;
	}
	
	public List<SimpleDto> getMarchesIdsWithTypeByProjet(Integer idProjet) {
		return 	entityManager.createQuery(""
				+ "SELECT new dto.SimpleDto(m.id, mt.nom) from Marches m join m.marchesType mt "
					+ "WHERE m.projet.id = :idProjet", SimpleDto.class)
				.setParameter("idProjet", idProjet)
				.getResultList()
				;
	}
	
	
	
}
