package security;




import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import entities.User;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = -871710995381646579L;
	
	private User userEntity;
	
	
	public UserPrincipal(User userEntity) {
		this.userEntity = userEntity;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		
		userEntity.getRoles().forEach((role) -> {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getLabel()));
			role.getPermissions().forEach((perm) -> grantedAuthorities.add(new SimpleGrantedAuthority(perm.getLabel())));
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
