package entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="projet_partenaire")
public class ProjetPartenaire implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
    @ManyToOne
    @JoinColumn(name = "projet")
    private Projet projet;

	@ManyToOne
	@JoinColumn(name = "partenaire")
	private Acheteur partenaire;
	

	
	private double financement;
	
	
	
	public ProjetPartenaire() {}

	public ProjetPartenaire(Projet projet, Acheteur partenaire, double financement) {
		this.projet = projet;
		this.partenaire = partenaire;
		this.financement = financement;
	}


	public Acheteur getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Acheteur partenaire) {
		this.partenaire = partenaire;
	}

	public double getFinancement() {
		return financement;
	}

	public void setFinancement(double financement) {
		this.financement = financement;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Projet getProjet() {
		return projet;
	}

	public void setProjet(Projet projet) {
		this.projet = projet;
	}


	
	
	

}
