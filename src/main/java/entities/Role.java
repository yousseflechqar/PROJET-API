package entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "role")
public class Role implements java.io.Serializable  {

	private static final long serialVersionUID = -235371405521235795L;

	@Id
	private Integer id;
	
	private String label;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
		name="roles_permissions",
		joinColumns = @JoinColumn(name="role", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name="permission", referencedColumnName = "id")		
	)
	private Set<Permission> permissions = new HashSet<Permission>(0);
	
	public Role() {}
	
	public Role(Integer id) {
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

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	
}
