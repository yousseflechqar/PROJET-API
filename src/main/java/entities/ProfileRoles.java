package entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="profile_roles")
public class ProfileRoles {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	
    @ManyToOne
    @JoinColumn(name = "profile")
    private Profile profile;

	@ManyToOne
	@JoinColumn(name = "role")
	private Role role;

	public ProfileRoles() {}
	
	public ProfileRoles(Profile profile, Role role) {
		super();
		this.profile = profile;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	
	
	
	
}
