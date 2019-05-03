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
@Table(name = "marches_os")
public class MarchesOs implements java.io.Serializable {

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marche")
	private Marches marches;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "os")
	private OsType osType;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_os")
	private Date dateOs;
	
	private String commentaire;

	public MarchesOs() {}

	public MarchesOs(Marches marches, OsType osType, Date dateOs, String commentaire) {
		this.marches = marches;
		this.osType = osType;
		this.dateOs = dateOs;
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


	public OsType getOsType() {
		return this.osType;
	}

	public void setOsType(OsType osType) {
		this.osType = osType;
	}


	public Date getDateOs() {
		return this.dateOs;
	}

	public void setDateOs(Date dateOs) {
		this.dateOs = dateOs;
	}


	public String getCommentaire() {
		return this.commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}
