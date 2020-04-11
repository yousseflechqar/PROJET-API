package security.models;




import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import config.SpringApplicationContext;
import entities.User;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = -871710995381646579L;
	
	private User userEntity;
//	private List<String> roles = new ArrayList<String>();
//	private List<String> permissions = new ArrayList<String>();
	
	
	public UserPrincipal(User userEntity) {
		this.userEntity = userEntity;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		
//		Environment env = (Environment)(SpringApplicationContext.getBean(Environment.class));
		Environment env = SpringApplicationContext.getBean(Environment.class);
		
		userEntity.getRoles().forEach((role) -> {
			
			grantedAuthorities.add(new SimpleGrantedAuthority(env.getProperty("security.role.prefix") + role.getLabel().toUpperCase()));
//			roles.add(role.getLabel());
			
			role.getPermissions().forEach((perm) -> {
				grantedAuthorities.add(new SimpleGrantedAuthority(perm.getLabel()));
//				permissions.add(perm.getLabel());
			});
		});
		
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return userEntity.getPassword();
	}

	@Override
	public String getUsername() {
		return userEntity.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return userEntity.isEnabled();
	}

	public User getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(User userEntity) {
		this.userEntity = userEntity;
	}

}
