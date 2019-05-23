package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beans.UserBean;
import dao.GenericDao;
import dao.UserDao;
import dto.SelectGrpDto;
import dto.SimpleDto;
import entities.Division;
import entities.Profile;
import entities.User;


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
		
		user.setDivision( bean.division != null ? new Division(bean.division) : null );
		
		user.setProfile(new Profile(bean.profile));
		
		if(bean.id == null) {
			gUserDao.create(user);
		} else {
			user.getUserRoles().clear();
		}
		
		entityManager.flush();
		
//		bean.roles.forEach( role -> {
//			user.getUserRoles().add(new UserRole(user, new Role(role)));
//		});
		
		return user.getId();
	}

	
	public UserBean getUserForEdit(Integer idUser) {
		
		User user = userDao.getUserForEdit(idUser);
		
		UserBean dto = new UserBean(
				user.getId(), user.getLogin(), user.getPassword(), user.getNom(), user.getPrenom(), user.isActive(), 
				user.getDivision(), user.getProfile().getId()
		);
		
//		user.getUserRoles().forEach(ur -> {
//			dto.roles.add(ur.getRole().getId());
//		});
		
		return dto;
	} 
	
	
	public Map<String, List<SimpleDto>> getChargesSuivi() {
		
		List<User> chargesSuivi = userDao.getChargesSuivi();
		
		Map<String, List<SimpleDto>> usersDivisionMap = new LinkedHashMap<String, List<SimpleDto>>();
		
		
		chargesSuivi.forEach( cs -> {
			String nomDiv = cs.getDivision().getNom();
			if( !usersDivisionMap.containsKey(nomDiv) ) {
				usersDivisionMap.put(nomDiv, new ArrayList<SimpleDto>());
			}
			
			usersDivisionMap.get(nomDiv).add(new SimpleDto(cs.getId(), cs.getNom() + " " + cs.getPrenom()));
		});
		
		return usersDivisionMap;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<SelectGrpDto> getChargesSuivi2() {
		
		List<User> chargesSuivi = userDao.getChargesSuivi();
		

		Map<Integer, SelectGrpDto> usersDivisionMap = new LinkedHashMap<Integer, SelectGrpDto>();
		
		
		chargesSuivi.forEach( cs -> {
			Integer idDiv = cs.getDivision().getId();
			if( !usersDivisionMap.containsKey(idDiv) ) {
				usersDivisionMap.put(idDiv, new SelectGrpDto(cs.getDivision().getNom()));
			}
			
			usersDivisionMap.get(idDiv).options.add(new SimpleDto(cs.getId(), cs.getNom() + " " + cs.getPrenom()));
		});
		
		return usersDivisionMap.values();
	}
	
}
