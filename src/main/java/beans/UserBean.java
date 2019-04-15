package beans;

import java.util.ArrayList;
import java.util.List;

public class UserBean {
	
	public Integer id;
	public String login;
	public String password;
	public String nom;
	public String prenom;
	public List<Integer> roles = new ArrayList<Integer>(); ;
	public boolean active;
	
	public UserBean() {}
	
	// for the dto user list
	public UserBean(Integer id, String nom, String prenom) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}
	
	// for save user
	public UserBean(Integer id, String login, String password, String nom, String prenom, boolean active) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.active = active;
	}
	
	
}
