package entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "marches_decomptes")
public class MarchesDecomptes implements java.io.Serializable {

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marche")
	private Marches marches;
	
	private double decompte;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_decompte")
	private Date dateDecompte;
	
	private String commentaire;

	public MarchesDecomptes() {
	}


	public MarchesDecomptes(Marches marches, double decompte, Date dateDecompte, String commentaire) {
		this.marches = marches;
		this.decompte = decompte;
		this.dateDecompte = dateDecompte;
		this.commentaire = commentaire;
	}




	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Marches getMarches() {
		return this.marches;
	}

	public void setMarches(Marches marches) {
		this.marches = marches;
	}


	public double getDecompte() {
		return this.decompte;
	}

	public void setDecompte(double decompte) {
		this.decompte = decompte;
	}

	public Date getDateDecompte() {
		return this.dateDecompte;
	}

	public void setDateDecompte(Date dateDecompte) {
		this.dateDecompte = dateDecompte;
	}


	public String getCommentaire() {
		return this.commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}
