package entities;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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
@Table(name = "marches")
public class Marches implements java.io.Serializable {

	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "projet")
	private Projet projet;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_marche")
	private MarchesType marchesType;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "current_taux")
	private MarchesTaux currentTaux;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "etat_marche")
	private MarchesEtat marchesEtat;
	
	private String intitule;
	private String numMarche;
	private Integer delaiExecution;
	private Double montant;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_plis", length = 10)
	private Date datePlis;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_approbation")
	private Date dateApprobation;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_os_commencement")
	private Date dateOrdreService;
	

	@Temporal(TemporalType.DATE)
	@Column(name = "date_reception_provisoire")
	private Date dateReceptionProvisoire;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_reception_definitive")
	private Date dateReceptionDefinitive;

	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "marches")
	private Set<MarchesSociete> marchesSocietes = new HashSet<MarchesSociete>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "marches")
	private Set<MarchesTaux> marchesTauxes = new HashSet<MarchesTaux>(0);
	
	
	

	public Marches() {
	}

	public Marches(Integer id) {
		this.id = id;
	}




	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

	public MarchesType getMarchesType() {
		return this.marchesType;
	}

	public void setMarchesType(MarchesType marchesType) {
		this.marchesType = marchesType;
	}


	

	public MarchesEtat getMarchesEtat() {
		return this.marchesEtat;
	}

	public void setMarchesEtat(MarchesEtat marchesEtat) {
		this.marchesEtat = marchesEtat;
	}


	public MarchesTaux getCurrentTaux() {
		return currentTaux;
	}

	public void setCurrentTaux(MarchesTaux currentTaux) {
		this.currentTaux = currentTaux;
	}


	public Projet getProjet() {
		return this.projet;
	}

	public void setProjet(Projet projet) {
		this.projet = projet;
	}



	
	

	public Date getDatePlis() {
		return datePlis;
	}

	public void setDatePlis(Date datePlis) {
		this.datePlis = datePlis;
	}

	public Date getDateOrdreService() {
		return this.dateOrdreService;
	}

	public void setDateOrdreService(Date dateOrdreService) {
		this.dateOrdreService = dateOrdreService;
	}

	public Date getDateApprobation() {
		return this.dateApprobation;
	}

	public void setDateApprobation(Date dateApprobation) {
		this.dateApprobation = dateApprobation;
	}

	public Date getDateReceptionProvisoire() {
		return this.dateReceptionProvisoire;
	}

	public void setDateReceptionProvisoire(Date dateReceptionProvisoire) {
		this.dateReceptionProvisoire = dateReceptionProvisoire;
	}

	public Date getDateReceptionDefinitive() {
		return dateReceptionDefinitive;
	}

	public void setDateReceptionDefinitive(Date dateReceptionDefinitive) {
		this.dateReceptionDefinitive = dateReceptionDefinitive;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getNumMarche() {
		return this.numMarche;
	}

	public void setNumMarche(String numMarche) {
		this.numMarche = numMarche;
	}

	public Integer getDelaiExecution() {
		return this.delaiExecution;
	}

	public void setDelaiExecution(Integer delaiExecution) {
		this.delaiExecution = delaiExecution;
	}

	public Double getMontant() {
		return this.montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}


	public Set<MarchesTaux> getMarchesTauxes() {
		return this.marchesTauxes;
	}

	public void setMarchesTauxes(Set<MarchesTaux> marchesTauxes) {
		this.marchesTauxes = marchesTauxes;
	}
	

	public Set<MarchesSociete> getMarchesSocietes() {
		return this.marchesSocietes;
	}

	public void setMarchesSocietes(Set<MarchesSociete> marchesSocietes) {
		this.marchesSocietes = marchesSocietes;
	}

}
