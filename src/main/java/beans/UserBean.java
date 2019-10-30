package beans;

import java.util.ArrayList;
import java.util.List;

import entities.Division;

public class UserBean {
	
	public Integer id;
	public String login;
	public String password;
	public String nom;
	public String prenom;
	public String email;
	public String phone;
	public Integer userType;
	public Integer division;

	public Boolean isDisable = false;

	public Boolean isChargeSuivi = false;
	
	public List<Integer> roles = new ArrayList<Integer>();
	
	
	public UserBean() {}
	
	// for the dto user list
	public UserBean(Integer id, String nom, String prenom) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}
	

	public UserBean(Integer id, String login, String password, String nom, String prenom, String email, String phone,
			Boolean isDisable, Boolean isChargeSuivi, Integer userType,
			Division division) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.phone = phone;
		this.isDisable = isDisable;
		this.isChargeSuivi = isChargeSuivi;
		this.userType = userType;
		this.division = division != null ? division.getId() : null;
	}
	
	
}
