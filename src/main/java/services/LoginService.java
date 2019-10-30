package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beans.LoginBean;
import dao.UserDao;
import dto.SimpleDto;
import dto.UserSession;
import entities.User;

@Service
public class LoginService {

	
	@Autowired
	private UserDao userDao;
	
	
	@Transactional(rollbackOn = Exception.class)
	public Object login(LoginBean bean, HttpServletRequest request, HttpSession session) {

		User user = userDao.checkUser(bean.login, bean.password);
		
		
		if( user == null ) {
			return 0;
		}
		
		if( user.isDisable() ) {
			return -1;
		}
		
	
		
		List<Integer> rolesByProfile = new ArrayList<Integer>();
		
		user.getUserRoles().forEach(ur -> {
			rolesByProfile.add(ur.getRole().getId());
		});
		
		UserSession userSession = new UserSession(user.getId(), user.getNom(), user.getPrenom(), user.getUserType().getId(), rolesByProfile);
		
		session.setAttribute("user", userSession);
		
		user.setLastConnexion(new Date());
		
		return userSession;
	}

	
	
}
