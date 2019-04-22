package entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "profile")
public class Profile implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
	private String label;

	
	
	public Profile() {}
	public Profile(Integer id) {
		super();
		this.id = id;
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
	
	
	
	
	
}
