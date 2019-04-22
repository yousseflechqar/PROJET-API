package entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "indh_programme")
public class IndhProgramme {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	private String label;
	private Integer phase;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	private IndhProgramme parentProgramme;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentProgramme", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<IndhProgramme> subProgrammes = new HashSet<IndhProgramme>(0);

	
	
	public IndhProgramme() {}
	public IndhProgramme(Integer id) {
		super();
		this.id = id;
	}
	
	public IndhProgramme(Integer id, String label, Integer phase, Set<IndhProgramme> subProgrammes) {
		super();
		this.id = id;
		this.label = label;
		this.phase = phase;
		this.subProgrammes = subProgrammes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public IndhProgramme getParentProgramme() {
		return parentProgramme;
	}

	public void setParentProgramme(IndhProgramme parentProgramme) {
		this.parentProgramme = parentProgramme;
	}

	public Set<IndhProgramme> getSubProgrammes() {
		return subProgrammes;
	}

	public void setSubProgrammes(Set<IndhProgramme> subProgrammes) {
		this.subProgrammes = subProgrammes;
	}

	public Integer getPhase() {
		return phase;
	}

	public void setPhase(Integer phase) {
		this.phase = phase;
	}
	
	
	
}
