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
public class Projet implements java.io.Serializable {

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	private String intitule;
	private Double montant;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "secteur")
	private Secteur secteur;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "maitre_ouvrage")
	private MaitreOuvrage maitreOuvrage;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "maitre_ouvrage_delegue")
	private MaitreOuvrage maitreOuvrageDelegue;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_saisie")
	private Date dateSaisie;
	
	// MAPPED BY
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<Localisation> localisations = new HashSet<Localisation>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<ProjetPartenaire> projetPartenaires = new HashSet<ProjetPartenaire>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<ProjetUser> projetUsers = new HashSet<ProjetUser>(0);
	
	public Projet() {}
	
	public Projet(Integer id) {
		this.id = id;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}

	public Secteur getSecteur() {
		return secteur;
	}

	public void setSecteur(Secteur secteur) {
		this.secteur = secteur;
	}

	public MaitreOuvrage getMaitreOuvrage() {
		return maitreOuvrage;
	}

	public void setMaitreOuvrage(MaitreOuvrage maitreOuvrage) {
		this.maitreOuvrage = maitreOuvrage;
	}

	public MaitreOuvrage getMaitreOuvrageDelegue() {
		return maitreOuvrageDelegue;
	}

	public void setMaitreOuvrageDelegue(MaitreOuvrage maitreOuvrageDelegue) {
		this.maitreOuvrageDelegue = maitreOuvrageDelegue;
	}

	public Date getDateSaisie() {
		return dateSaisie;
	}

	public void setDateSaisie(Date dateSaisie) {
		this.dateSaisie = dateSaisie;
	}

	public Set<Localisation> getLocalisations() {
		return localisations;
	}

	public void setLocalisations(Set<Localisation> localisations) {
		this.localisations = localisations;
	}

	public Set<ProjetPartenaire> getProjetPartenaires() {
		return projetPartenaires;
	}

	public void setProjetPartenaires(Set<ProjetPartenaire> projetPartenaires) {
		this.projetPartenaires = projetPartenaires;
	}

	public Set<ProjetUser> getProjetUsers() {
		return projetUsers;
	}

	public void setProjetUsers(Set<ProjetUser> projetUsers) {
		this.projetUsers = projetUsers;
	}



	
}
