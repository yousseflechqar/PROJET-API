package services;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beans.UserBean;
import dao.GenericDao;
import dao.UserDao;
import dto.PartnerDto;
import dto.ProjetEditDto;
import dto.SimpleDto;
import entities.Projet;
import entities.Role;
import entities.User;
import entities.UserRole;


@Service
public class UserService {

	
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private UserDao userDao;
	@Autowired
	private GenericDao<User, Integer> gUserDao;
	
	@Transactional(rollbackOn = Exception.class)
	public Integer saveUser(UserBean bean) {

		User user = bean.id != null ? userDao.getUserForEdit(bean.id) : new User();
		
		user.setLogin(bean.login);
		user.setPassword(bean.password);
		user.setNom(bean.nom);
		user.setPrenom(bean.prenom);
		user.setActive(bean.active);
		user.setDateCreation(new Date());
		
		if(bean.id == null) {
			gUserDao.create(user);
		} else {
			user.getUserRoles().clear();
		}
		
		entityManager.flush();
		
		bean.roles.forEach( role -> {
			user.getUserRoles().add(new UserRole(user, new Role(role)));
		});
		
		return user.getId();
	}

	
	public UserBean getUserForEdit(Integer idUser) {
		
		User user = userDao.getUserForEdit(idUser);
		
		UserBean dto = new UserBean(
				user.getId(), user.getLogin(), user.getPassword(), user.getNom(), user.getPrenom(), user.isActive()
		);
		
		user.getUserRoles().forEach(ur -> {
			dto.roles.add(ur.getRole().getId());
		});
		
		return dto;
	} 
	
}
