package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import beans.UserBean;
import dao.GenericDao;
import dao.UserDao;
import dto.ProjetEditDto;
import dto.SimpleDto;
import entities.Projet;
import entities.User;
import services.UserService;

@RestController
@RequestMapping(value = "/api")
public class UserRest {

	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private GenericDao<User, Integer> gUserDao;
	
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
	
	@GetMapping(value = "/profiles")
	public List<SimpleDto> getListProfiles() {
		return userDao.getListProfiles();
	}
	
	
}
