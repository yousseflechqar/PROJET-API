package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import beans.LocalisationBean;
import beans.SimpleBean;
import dto.ProjetDto;
import dto.ProjetEditDto;
import dto.SimpleDto;
import entities.Projet;



@Repository
public class ProjetDao {
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	

	public Projet getProjetForEdit(Integer idProjet){
		
		try {
			return (Projet) entityManager.createQuery(""
					+ "SELECT p FROM Projet p "
						+ "LEFT JOIN FETCH p.maitreOuvrage "
						+ "LEFT JOIN FETCH p.maitreOuvrageDelegue "
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
		
		return entityManager.createQuery("SELECT new dto.ProjetDto(p.id, p.intitule) FROM Projet p")
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<SimpleDto> getSecteurs(){
		return entityManager.createQuery("SELECT new dto.SimpleDto(c.id, c.nom) FROM Secteur c ")
				.getResultList() ;
	}
	@SuppressWarnings("unchecked")
	public List<SimpleDto> getAcheteursByName(String q){
		return entityManager.createQuery("SELECT new dto.SimpleDto(c.id, c.nom) FROM MaitreOuvrage c WHERE c.nom LIKE :q")
				.setParameter("q", "%" + q + "%")
				.getResultList() ;
	}
	
	@SuppressWarnings("unchecked")
	public List<SimpleDto> getPartnersByName(String q) {
		return entityManager.createQuery("SELECT new dto.SimpleDto(id, nom) FROM Partenaire WHERE nom LIKE :q")
				.setParameter("q", "%" + q + "%")
				.getResultList() ;
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
