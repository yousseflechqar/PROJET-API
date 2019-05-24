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
	

	private Double financement;
	private String commentaire;
	
	
	
	public ProjetPartenaire() {}

	public ProjetPartenaire(Projet projet, Acheteur partenaire, Double financement, String commentaire) {
		this.projet = projet;
		this.partenaire = partenaire;
		this.financement = financement;
		this.commentaire = commentaire;
	}


	public Acheteur getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(Acheteur partenaire) {
		this.partenaire = partenaire;
	}

	public Double getFinancement() {
		return financement;
	}

	public void setFinancement(Double financement) {
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

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}


	
	
	

}
