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
@Table(name = "programme")
public class Programme {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	private String label;
	private Integer phase;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	private Programme parentProgramme;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentProgramme", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<Programme> subProgrammes = new HashSet<Programme>(0);

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

	public Programme getParentProgramme() {
		return parentProgramme;
	}

	public void setParentProgramme(Programme parentProgramme) {
		this.parentProgramme = parentProgramme;
	}

	public Set<Programme> getSubProgrammes() {
		return subProgrammes;
	}

	public void setSubProgrammes(Set<Programme> subProgrammes) {
		this.subProgrammes = subProgrammes;
	}

	public Integer getPhase() {
		return phase;
	}

	public void setPhase(Integer phase) {
		this.phase = phase;
	}
	
	
	
}
