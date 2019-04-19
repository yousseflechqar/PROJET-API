package dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import beans.LocalisationBean;
import beans.ParentChildBean;
import beans.ProgrammeBean;
import dto.ProjetDto;
import dto.SimpleDto;
import entities.IndhProgramme;
import entities.Projet;



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
		
		return entityManager.createQuery("SELECT new dto.ProjetDto(p.id, p.intitule) FROM Projet p")
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<SimpleDto> getSecteurs(){
		return entityManager.createQuery("SELECT new dto.SimpleDto(c.id, c.nom) FROM Secteur c")
				.getResultList() ;
	}
	@SuppressWarnings("unchecked")
	public List<SimpleDto> getAcheteursByName(String q){
		return entityManager.createQuery("SELECT new dto.SimpleDto(c.id, c.nom) FROM Acheteur c WHERE c.nom LIKE :q")
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

	@SuppressWarnings("unchecked")
	public List<SimpleDto> getFinancements(Integer acheteur) {
		return entityManager.createQuery("SELECT new dto.SimpleDto(sf.id, sf.label) "
					+ "FROM AcheteurSrcFinancement ach_sf "
						+ "JOIN ach_sf.srcFinancement sf "
						+ "WHERE ach_sf.acheteur.id = :maitreOuvrage")
				.setParameter("maitreOuvrage", acheteur)
				.getResultList() ;
	}

//	@SuppressWarnings("unchecked")
//	public List<SimpleDto> getProgrammes2() {
//		return entityManager.createQuery("SELECT new dto.SimpleDto(p.id, p.label) FROM Programme p")
//				.getResultList() ;
//	}
	
	@SuppressWarnings("unchecked")
	public List<ProgrammeBean> getParentProgrammes() {
		return entityManager.createQuery(""
				+ "SELECT new beans.ProgrammeBean(p.id, p.label, parent.id, parent.label, p.phase) FROM IndhProgramme p "
					+ "LEFT JOIN p.parentProgramme parent "
				+ "WHERE p.parentProgramme IS NULL"
		)
		.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<IndhProgramme> getIndhProgrammes() {
		return entityManager.createQuery("SELECT p FROM IndhProgramme p").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProgrammeBean> getIndhProgrammes2() {
		
		return entityManager.createQuery(""
				+ "SELECT new beans.ProgrammeBean(p.id, p.label, parent.id, parent.label, p.phase) FROM IndhProgramme p "
					+ "LEFT JOIN p.parentProgramme parent "
		)
		.getResultList();
		
	}

}
