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
	private Partenaire partenaire;
	
	private double financement;
	
	
	
	public ProjetPartenaire() {}

	public ProjetPartenaire(Projet projet, Partenaire partenaire, double financement) {
		this.projet = projet;
		this.partenaire = partenaire;
		this.financement = financement;
	}

	public Partenaire getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Partenaire partenaire) {
		this.partenaire = partenaire;
	}

	public double getFinancement() {
		return financement;
	}

	public void setFinancement(double financement) {
		this.financement = financement;
	}
	
	
	
	
	

}
