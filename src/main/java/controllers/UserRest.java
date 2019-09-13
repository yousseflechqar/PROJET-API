package controllers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import beans.LoginBean;
import beans.UserBean;
import dao.GenericDao;
import dao.UserDao;
import dto.SelectGrpDto;
import dto.SimpleDto;
import entities.User;
import services.LoginService;
import services.UserService;

@RestController
@RequestMapping(value = "/api")
public class UserRest {

	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private GenericDao<User, Integer> gUserDao;
	
	@PostMapping(value = "/login")
	public Object login(@RequestBody LoginBean bean, HttpServletRequest request, HttpSession session) {
		
		return loginService.login(bean, request, session);
	}
	
	@RequestMapping(value="/logout") 
	public void logout(HttpServletRequest request) {
		request.getSession().invalidate();
	}
	
	@PostMapping(value = "/users")
	public Integer saveUser(@RequestBody UserBean bean) {
		
		return userService.saveUser(bean);
	}
	
	@GetMapping(value = "/users/edit/{idUser}")
	public UserBean getProjetForEdit(@PathVariable Integer idUser) {
		return userService.getUserForEdit(idUser);
	}
	
	@GetMapping(value = "/users")
	public List<UserBean> getListUsers() {
		return userDao.getListUsers();
	}
	
	@DeleteMapping(value = "/users/{idUser}")
	public void deleteUser(@PathVariable Integer idUser) {
		
		gUserDao.delete(User.class, idUser);
	}
	
	
	@GetMapping(value = "/chargesSuivi")
	public Collection<SelectGrpDto> getChargesSuivi() {
		return userService.getChargesSuivi2();
	}
	
	
	@GetMapping(value = "/profiles")
	public List<SimpleDto> getListProfiles() { return userDao.getListProfiles(); }
	
	@GetMapping(value = "/roles")
	public List<SimpleDto> getListRoles() { return userDao.getListRoles(); }
	
	@PostMapping(value = "/roles/{idProfile}")
	public void saveRolesToProfile(@PathVariable Integer idProfile, @RequestBody List<Integer> roles) { 
		userService.saveRolesToProfile(idProfile, roles); 
	}
	
	@GetMapping(value = "/roles/{idProfile}")
	public List<SimpleDto> getRolesByProfile(@PathVariable Integer idProfile) { return userDao.getRolesByProfile(idProfile); }
	

	
	@GetMapping(value = "/divisions")
	public List<SimpleDto> getDivisions() { return userDao.getDivisions(); }
	
	
}
