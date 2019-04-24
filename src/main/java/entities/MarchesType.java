package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "marches_type")
public class MarchesType implements java.io.Serializable {

	@Id
	private Integer id;
	private String nom;

	public MarchesType() {
	}
	public MarchesType(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "nom", length = 60)
	public String getNom() {
		return this.nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}


}
