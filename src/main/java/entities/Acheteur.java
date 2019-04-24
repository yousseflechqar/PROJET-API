package entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name = "acheteur")
public class Acheteur implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nom;
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acheteur", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<AcheteurSrcFinancement> acheteurSrcFinancements = new HashSet<AcheteurSrcFinancement>(0);

	
	public Acheteur() {}
	
	public Acheteur(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	
	
}
