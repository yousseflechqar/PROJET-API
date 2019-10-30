package entities;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "user")
public class User implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	
	

	
	private String login;
	private String password;
	private String email;
	private String nom;
	private String prenom;
	private String telephone;
	
	private boolean disable = false;
	
	@Column(name = "charge_suivi")
	private Boolean chargeSuivi;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_connexion")
	private Date lastConnexion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_creation")
	private Date dateCreation;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_type")
	private UserType userType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "division")
	private Division division;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private Set<UserRoles> userRoles = new HashSet<UserRoles>(0);
	
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<ProjetUser> projetUsers = new HashSet<ProjetUser>(0);
	

	
	public User() {
	}
	public User(Integer id) {
		this.id = id;
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Division getDivision() {
		return this.division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}


	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}


	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Date getLastConnexion() {
		return this.lastConnexion;
	}
	public void setLastConnexion(Date lastConnexion) {
		this.lastConnexion = lastConnexion;
	}
	public Date getDateCreation() {
		return this.dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Set<ProjetUser> getProjetUsers() {
		return this.projetUsers;
	}

	public void setProjetUsers(Set<ProjetUser> projetUsers) {
		this.projetUsers = projetUsers;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}


	public Boolean isChargeSuivi() {
		return chargeSuivi;
	}
	public void setChargeSuivi(Boolean chargeSuivi) {
		this.chargeSuivi = chargeSuivi;
	}
	public UserType getUserType() {
		return userType;
	}
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	public Boolean getChargeSuivi() {
		return chargeSuivi;
	}
	public Set<UserRoles> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(Set<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}

}
