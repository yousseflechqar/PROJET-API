package security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import config.SpringApplicationContext;
import dao.UserDao;
import enums.PermissionEnum;
import enums.RoleEnum;
import exceptions.ForbiddenException;


@Component("securityUtils")
public class SecurityUtils {
	

	static public List<String> editProjectSuperRoles() {
		return Arrays.asList(RoleEnum.ADMIN.getKey(), RoleEnum.SUPERVISOR_DIV.getKey());
	}
	
	static public boolean accessEditProjectSuperRoles() {
		return SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().stream().anyMatch( authority -> editProjectSuperRoles().stream()
						.anyMatch(editProjectRole -> editProjectRole.equals(authority.getAuthority()))) ;
	}
	
//	static public boolean accessChargeSuiviRole() {
//		return accessAuthorities(RoleEnum.CHARGE_SUIVI.getKey()) ;
//	}
	
	
	
	static public boolean accessAuthorities(String authority) {
		
		return SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().stream().anyMatch( auth -> auth.getAuthority().equals(authority) );
		
	}
	
	
	static public boolean deleteProjectPermissionsChecks() {
		
		if( accessAuthorities(PermissionEnum.DELETE_PROJECT.getKey()) ) {
			return true;
		}
		
		throw new ForbiddenException("Vous n'avez pas le droit de supprimer ce projet");
	}
	
	static public boolean editProjectPermissionsChecks() {
		
		if( accessAuthorities(PermissionEnum.EDIT_PROJECT.getKey()) ) {
			return true;
		}
		
		throw new ForbiddenException("Vous n'avez pas le droit de modifier ce projet");
	}
	

	static public boolean saveEditedProjectChecks(Integer currentUser, Integer idProjet) {

		UserDao userDao = (UserDao)(SpringApplicationContext.getBean(UserDao.class));

		
		if( accessEditProjectSuperRoles() || 
				( accessAuthorities(RoleEnum.CHARGE_SUIVI.getKey()) && userDao.getChargeSuiviByProj(idProjet).equals(currentUser) ) 
		) {
			return true;
		}
		
		throw new ForbiddenException("Vous n'avez pas le droit de modifier ce projet");
	}




}
