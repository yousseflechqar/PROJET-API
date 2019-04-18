package entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name = "commune")
public class Commune implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	private String nom;
	private Boolean rurale;
	
	
	
	public Commune() {}

	public Commune(Integer id) {
		this.id = id;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "commune")
	private Set<Fraction> fractions = new HashSet<Fraction>(0);

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
	public Boolean getRurale() {
		return this.rurale;
	}
	public void setRurale(Boolean rurale) {
		this.rurale = rurale;
	}
	


}
