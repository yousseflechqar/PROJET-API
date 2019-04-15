package entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "src_financement")
public class SrcFinancement implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	private String label;


	public SrcFinancement() {}
	public SrcFinancement(Integer id) {
		this.id = id;
	}
	public SrcFinancement(String label) {
		this.label = label;
	}




	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getLabel() {
		return this.label;
	}
	public void setLabel(String label) {
		this.label = label;
	}



}
