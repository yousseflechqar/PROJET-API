package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import beans.UserBean;
import dto.SimpleDto;
import entities.Division;
import entities.Profile;
import entities.Role;
import entities.User;
import enums.ProfilesEnum;

@Repository
public class UserDao {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	public User checkUser(String login, String password){
		
		try {
			return entityManager.createQuery(""
					
					 + " SELECT u FROM User u "
					 	+ " WHERE login = :login AND password = :password ", User.class)
					
			.setParameter("login", login)
			.setParameter("password", password)
			.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	
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
	public List<SimpleDto> getListRoles() {
		return entityManager.createQuery("SELECT new dto.SimpleDto(r.id, r.label) FROM Role r")
				.getResultList() ;
	}
	
	@SuppressWarnings("unchecked")
	public List<SimpleDto> getListProfiles() {
		return entityManager.createQuery("SELECT new dto.SimpleDto(r.id, r.label) FROM Profile r")
				.getResultList() ;
	}
	

	public List<SimpleDto> getDivisions() {
		return entityManager.createQuery("SELECT new dto.SimpleDto(d.id, d.nom) FROM Division d", SimpleDto.class).getResultList() ;
	}


	public List<User> getChargesSuivi() {
		return entityManager.createQuery(""
				+ "SELECT u FROM User u "
					+ "LEFT JOIN FETCH u.division "
					+ "JOIN u.profile prf "
				+ "WHERE prf.id = :profile", User.class)
				.setParameter("profile", ProfilesEnum.charge_suivi.value)
				.getResultList();
	}



	public List<SimpleDto> getRolesByProfile(Integer idProfile) {
		return entityManager.createQuery(""
				+ "SELECT new dto.SimpleDto(r.id, r.label) FROM ProfileRoles pR "
					+ "JOIN pR.role r "
				+ "WHERE pR.profile.id = :idProfile", SimpleDto.class)
				.setParameter("idProfile", idProfile)
				.getResultList();
	}
	
	public List<Integer> getRolesByProfile2(Integer idProfile) {
		return entityManager.createQuery(""
				+ "SELECT r.id FROM ProfileRoles pR "
				+ "JOIN pR.role r "
				+ "WHERE pR.profile.id = :idProfile", Integer.class)
				.setParameter("idProfile", idProfile)
				.getResultList();
	}


	public void deleteRolesByProfile(Integer idProfile) {

		entityManager.createQuery(""
				+ "DELETE FROM ProfileRoles pR WHERE pR.profile.id = :idProfile")
				.setParameter("idProfile", idProfile)
				.executeUpdate();;
		
	}
	
}





