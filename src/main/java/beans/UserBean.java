package beans;

import java.util.ArrayList;
import java.util.List;

public class UserBean {
	
	public Integer idUser;
	public String login;
	public String password;
	public String nom;
	public String prenom;
	public List<Integer> roles = new ArrayList<Integer>(); ;
	public boolean active;
	
	public UserBean() {}
	
	// for the dto user list
	public UserBean(Integer idUser, String nom, String prenom) {
		this.idUser = idUser;
		this.nom = nom;
		this.prenom = prenom;
	}
	
	// for save user
	public UserBean(Integer idUser, String login, String password, String nom, String prenom, boolean active) {
		this.idUser = idUser;
		this.login = login;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.active = active;
	}
	
	
}
