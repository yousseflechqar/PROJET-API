package entities;
// Generated 09-Apr-2019 01:03:48 by Hibernate Tools 5.2.6.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "division")
public class Division implements java.io.Serializable {

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	private String nom;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "division")
	private Set<User> users = new HashSet<User>(0);

	public Division() {
	}

	public Division(Integer id) {
		this.id = id;
	}
	
	public Division(String nom) {
		this.nom = nom;
	}

	public Division(String nom, Set<User> users) {
		this.nom = nom;
		this.users = users;
	}




	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}


	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
