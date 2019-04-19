package entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "projet_indh")
public class ProjetIndh {
	
	@Id
	private Integer id;
	 
	@OneToOne
    @JoinColumn(name = "projet")
	@MapsId
	private Projet projet;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "programme")
	private IndhProgramme programme;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sous_programme")
	private IndhProgramme subProgramme;

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

	public IndhProgramme getProgramme() {
		return programme;
	}

	public void setProgramme(IndhProgramme programme) {
		this.programme = programme;
	}

	public IndhProgramme getSubProgramme() {
		return subProgramme;
	}

	public void setSubProgramme(IndhProgramme subProgramme) {
		this.subProgramme = subProgramme;
	}
	
	
	
	

}
