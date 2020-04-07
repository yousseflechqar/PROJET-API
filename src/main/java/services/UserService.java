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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import beans.UserBean;
import dao.GenericDao;
import dao.UserDao;
import dto.SelectGrpDto;
import dto.SimpleDto;
import entities.Division;
import entities.Role;
import entities.User;
import entities.UserRoles;
import entities.UserType;
import security.UserPrincipal;


@Service
public class UserService implements UserDetailsService {

	
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private UserDao userDao;
	@Autowired
	private GenericDao<User, Integer> gUserDao;

	
	@Transactional(rollbackOn = Exception.class)
	public Integer saveUser(UserBean bean) {

		boolean isNewUser = ( bean.id == null );
		
		User user = ( isNewUser ) ? new User() : userDao.getUserForEdit(bean.id) ;
		
		user.setLogin(bean.login);
		user.setPassword(bean.password);
		user.setNom(bean.nom);
		user.setPrenom(bean.prenom);
		user.setTelephone(bean.phone);
		user.setEmail(bean.email);
		
		
		user.setEnabled(bean.enabled);
		
		user.setDivision( (bean.division != null) ? new Division(bean.division) : null );
		
		
		if( isNewUser ) {
			user.setDateCreation(new Date());
			gUserDao.persist(user);
		} else {
			user.getRoles().clear();
		}
		
		entityManager.flush();
		
		bean.roles.forEach( role -> {
			user.getRoles().add(new Role(role));
		});
		
		return user.getId();
	}

	
	public UserBean getUserForEdit(Integer idUser) {
		
		User user = userDao.getUserForEdit(idUser);
		
		UserBean dto = new UserBean(
				user.getId(), user.getLogin(), user.getPassword(), user.getNom(), user.getPrenom(), user.getEmail(), user.getTelephone(),
				user.isEnabled(), user.getDivision()
		);
		
		user.getRoles().forEach(role -> {
			dto.roles.add(role.getId());
		});
		
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
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<SelectGrpDto<SimpleDto>> getChargesSuivi2() {
		
		List<User> chargesSuivi = userDao.getChargesSuiviWithDivision();
		

		Map<Integer, SelectGrpDto<SimpleDto>> usersDivisionMap = new LinkedHashMap<>();
		
		
		chargesSuivi.forEach( cs -> {
			Integer idDiv = cs.getDivision().getId();
			if( !usersDivisionMap.containsKey(idDiv) ) {
				usersDivisionMap.put(idDiv, new SelectGrpDto<SimpleDto>(cs.getDivision().getNom()));
			}
			
			usersDivisionMap.get(idDiv).options.add(new SimpleDto(cs.getId(), cs.getNom() + " " + cs.getPrenom()));
		});
		
		return usersDivisionMap.values();
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return new UserPrincipal(userDao.fetchUserByLogin(username));
	}


}
