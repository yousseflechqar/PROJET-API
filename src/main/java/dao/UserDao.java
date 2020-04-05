package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import beans.UserBean;
import dto.SimpleDto;
import entities.Division;
import entities.Role;
import entities.User;


@Repository
public class UserDao implements UserDetailsService {

	
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
						+ "LEFT JOIN FETCH u.userRoles "
					+ "WHERE u.id = :idUser"
					)
					.setParameter("idUser", idUser)
					.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	
	public Integer getChargeSuiviByProj(Integer idProj) {


		return entityManager.createQuery("SELECT chargeSuivi.id FROM Projet p WHERE p.id = :idProj", Integer.class)
					.setParameter("idProj", idProj)
					.getResultList()
					.stream().findFirst().orElse(null);


	}
	
	
	public Integer getChargeSuiviByMarche(Integer idMarch) {
		
		
		return entityManager.createQuery(""
				+ "SELECT p.chargeSuivi.id FROM Marches m "
					+ "JOIN m.projet p "
				+ "WHERE m.id = :idMarch", Integer.class)
				.setParameter("idMarch", idMarch)
				.getResultList()
				.stream().findFirst().orElse(null);
		
		
	}



	public List<SimpleDto> getListRoles() {
		return entityManager.createQuery("SELECT new dto.SimpleDto(r.id, r.label) FROM Role r", SimpleDto.class).getResultList();
	}
	

	

	public List<SimpleDto> getDivisions() {
		return entityManager.createQuery("SELECT new dto.SimpleDto(d.id, d.nom) FROM Division d", SimpleDto.class).getResultList() ;
	}


	public List<User> getChargesSuivi() {
		return entityManager.createQuery("SELECT u FROM User u WHERE u.chargeSuivi = 1", User.class).getResultList();
	}
	
	public List<User> getChargesSuiviWithDivision() {
		return entityManager.createQuery("SELECT u FROM User u "
				+ "LEFT JOIN FETCH u.division "
				+ "WHERE u.chargeSuivi = 1", User.class).getResultList();
	}



	public List<SimpleDto> getUserTypes() {
		return entityManager.createQuery("SELECT new dto.SimpleDto(ut.id, ut.label) FROM UserType ut", SimpleDto.class).getResultList();
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		try {
			
			User dbUser = entityManager
							.createQuery("SELECT u FROM User u WHERE login = :login", User.class)
							.setParameter("login", username)
							.getSingleResult();
			
			return new org.springframework.security.core.userdetails.User(
					dbUser.getLogin(), dbUser.getPassword(), new ArrayList<>());
		}
		catch (NoResultException e) {
			throw new UsernameNotFoundException(username);
		}
		
		
		
	}



}





