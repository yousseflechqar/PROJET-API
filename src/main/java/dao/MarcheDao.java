package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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


	@Transactional
	public void setDefaultMarche(Integer idProjet, Integer idMarche) {

		entityManager.createQuery(" Update Projet p set p.defaultMarche.id = :idMarche where p.id = :idProjet ")
			.setParameter("idMarche", idMarche)
			.setParameter("idProjet", idProjet)
		.executeUpdate();
	}
	
	public Integer getDefaultMarche(Integer idProjet) {
		
		return entityManager.createQuery("SELECT defaultMarche.id from Projet WHERE id = :idProjet", Integer.class)
				.setParameter("idProjet", idProjet)
				.getSingleResult()
				;
	}
	
	public Integer getTravauxMarcheId(Integer idProjet) {

		return 	
		entityManager.createQuery(""
			+ "SELECT m.id from Marches m "
				+ "LEFT JOIN m.startOs startOs "
					+ "WHERE m.projet.id = :idProjet AND m.marchesType.id = :mType "
				+ "ORDER BY startOs.dateOs DESC, m.id DESC "
			+ "", Integer.class)
		
		.setParameter("idProjet", idProjet)
		.setParameter("mType", MarcheTypeEnum.travaux.value)
		
		.getResultList().stream().findFirst().orElse(null);
		

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
