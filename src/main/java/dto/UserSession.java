package dto;

import java.util.ArrayList;
import java.util.List;

public class UserSession {
	
	public Integer id;
	public String nom;
	public String prenom;
	public Integer userType;
	public List<Integer> roles = new ArrayList<>();
	
	
	
	public UserSession() {}
	
	public UserSession(Integer id, String nom, String prenom, Integer userType, List<Integer> roles) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.userType = userType;
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserSession [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", profile=" + userType + ", roles="
				+ roles + "]";
	}

	


}
