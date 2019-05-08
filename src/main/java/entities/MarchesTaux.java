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
@Table(name = "marches_taux")
public class MarchesTaux implements java.io.Serializable {

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marches")
	private Marches marches;
	
	private Integer taux;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_taux")
	private Date dateTaux;
	
	private String commentaire;

	public MarchesTaux() {
	}

	public MarchesTaux(Integer id) {
		this.id = id;
	}
	
	public MarchesTaux(Marches marches, Integer taux, Date dateTaux, String commentaire) {
		this.marches = marches;
		this.taux = taux;
		this.dateTaux = dateTaux;
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


	public Integer getTaux() {
		return this.taux;
	}

	public void setTaux(Integer taux) {
		this.taux = taux;
	}


	public Date getDateTaux() {
		return this.dateTaux;
	}

	public void setDateTaux(Date dateTaux) {
		this.dateTaux = dateTaux;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	
	
	
	

}
