package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "acheteur_financement")
public class AcheteurSrcFinancement implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acheteur")
	private Acheteur acheteur;
	
	@ManyToOne
	@JoinColumn(name = "src_financement")
	private SrcFinancement srcFinancement;

	public AcheteurSrcFinancement() {}

	public AcheteurSrcFinancement(Acheteur acheteur, SrcFinancement srcFinancement) {
		this.acheteur = acheteur;
		this.srcFinancement = srcFinancement;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Acheteur getAcheteur() {
		return acheteur;
	}

	public void setAcheteur(Acheteur acheteur) {
		this.acheteur = acheteur;
	}

	public SrcFinancement getSrcFinancement() {
		return srcFinancement;
	}

	public void setSrcFinancement(SrcFinancement srcFinancement) {
		this.srcFinancement = srcFinancement;
	}



}
