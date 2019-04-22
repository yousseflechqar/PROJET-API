package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import beans.UserBean;
import dto.SimpleDto;
import entities.User;

@Repository
public class UserDao {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@SuppressWarnings("unchecked")
	public List<UserBean> getListUsers(){
		
		return entityManager.createQuery("SELECT new beans.UserBean(u.id, u.nom, u.prenom) FROM User u")
				.getResultList();
	}


	public User getUserForEdit(Integer idUser) {
		try {
			return (User) entityManager.createQuery(""
					+ "SELECT u FROM User u "
//						+ "LEFT JOIN FETCH u.userRoles "
					+ "WHERE u.id = :idUser"
					)
					.setParameter("idUser", idUser)
					.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	public List<SimpleDto> getListRoles2() {
		return entityManager.createQuery("SELECT new dto.SimpleDto(r.id, r.label) FROM Role r")
				.getResultList() ;
	}
	
	@SuppressWarnings("unchecked")
	public List<SimpleDto> getListProfiles() {
		return entityManager.createQuery("SELECT new dto.SimpleDto(r.id, r.label) FROM Profile r")
				.getResultList() ;
	}
}
