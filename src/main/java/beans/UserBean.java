package beans;

import entities.Division;

public class UserBean {
	
	public Integer id;
	public String login;
	public String password;
	public String nom;
	public String prenom;
	public Integer profile;
	public Integer division;
	public boolean active;
	
	public UserBean() {}
	
	// for the dto user list
	public UserBean(Integer id, String nom, String prenom) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}
	
	// for save user
	public UserBean(Integer id, String login, String password, String nom, String prenom, boolean active, Integer division, Integer profile) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.active = active;
		this.profile = profile;
		this.division = division;
	}
	
	public UserBean(Integer id, String login, String password, String nom, String prenom, boolean active, Division division, Integer profile) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.active = active;
		this.profile = profile;
		this.division = division != null ? division.getId() : null;
	}
	
	
}
