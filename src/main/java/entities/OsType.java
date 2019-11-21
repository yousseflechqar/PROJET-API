package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "os_type")
public class OsType implements java.io.Serializable {

	@Id
	private Integer id;
	private String label;


	public OsType() {
	}

	public OsType(Integer id, String label) {
		this.id = id;
		this.label = label;
	}
	public OsType(Integer id) {
		this.id = id;
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
