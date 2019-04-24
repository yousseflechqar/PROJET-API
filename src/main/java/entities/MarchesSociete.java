package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "marches_societe")
public class MarchesSociete implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marche")
	private Marches marches;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "societe")
	private Societe societe;

	public MarchesSociete() {
	}

	public MarchesSociete(Marches marches, Societe societe) {
		this.marches = marches;
		this.societe = societe;
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


	public Societe getSociete() {
		return this.societe;
	}

	public void setSociete(Societe societe) {
		this.societe = societe;
	}

}
