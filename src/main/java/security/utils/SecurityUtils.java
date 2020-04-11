package security.utils;

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
	
	
	private static UserDao userDao;
	
	
	@Autowired
	public SecurityUtils(UserDao userDao) {
		SecurityUtils.userDao = userDao;
	}

	static public List<String> editProjectSuperRoles() {
		return Arrays.asList(RoleEnum.ADMIN.getKey(), RoleEnum.SUPERVISOR_DIV.getKey());
	}
	
	static public boolean accessEditProjectSuperRoles() {
		return SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().stream().anyMatch( authority -> editProjectSuperRoles().stream()
						.anyMatch(editProjectRole -> editProjectRole.equals(authority.getAuthority()))) ;
	}
	
	static public boolean isChargeSuivi() {
		return accessAuthorities(RoleEnum.CHARGE_SUIVI.getKey()) ;
	}
	
	static public boolean canAssignProject() {
		return accessAuthorities(PermissionEnum.ASSIGN_PROJECT.getKey()) ;
	}
	

	static public boolean saveEditedProjectChecks(Integer currentUser, Integer idProjet) {

//		UserDao userDao = (UserDao)(SpringApplicationContext.getBean(UserDao.class));

		
		if( accessEditProjectSuperRoles() || 
			( accessAuthorities(RoleEnum.CHARGE_SUIVI.getKey()) && userDao.getChargeSuiviByProj(idProjet).equals(currentUser) ) 
		) {
			return true;
		}
		
		throw new ForbiddenException("Vous n'avez pas le droit de modifier ce projet");
	}
	
	static public boolean saveEditedMarcheChecks(Integer currentUser, Integer idMarche) {
		
//		UserDao userDao = (UserDao)(SpringApplicationContext.getBean(UserDao.class));
		
		
		if( accessEditProjectSuperRoles() || 
			( accessAuthorities(RoleEnum.CHARGE_SUIVI.getKey()) && userDao.getChargeSuiviByMarche(idMarche).equals(currentUser) ) 
		) {
			return true;
		}
		
		throw new ForbiddenException("Vous n'avez pas le droit de modifier ce marché");
	}
	
	
	static public boolean editProjectPermissionsChecks() {
		
		return genericPermissionsChecks(PermissionEnum.EDIT_PROJECT.getKey(), 
				"Vous n'avez pas le droit d'ajouter un projet/marché");
	}
	

	static public boolean deleteProjectPermissionsChecks() {
		
		return genericPermissionsChecks(PermissionEnum.DELETE_PROJECT.getKey(), 
				"Vous n'avez pas le droit de supprimer ce projet");
	}

	
	static public boolean viewUserPermissionsChecks() {
		
		return genericPermissionsChecks(PermissionEnum.VIEW_USERS.getKey(), 
				"Vous n'avez pas le droit de visualiser les utilisateurs");

	}
	
	static public boolean editUserPermissionsChecks() {
		
		return genericPermissionsChecks(PermissionEnum.EDIT_USER.getKey(), 
				"Vous n'avez pas le droit de modifier les utilisateurs");
		
	}
	
	static public boolean deleteUserPermissionsChecks() {
		
		return genericPermissionsChecks(PermissionEnum.DELETE_USER.getKey(), 
				"Vous n'avez pas le droit de supprimer les utilisateurs");
		
	}
	
	static public boolean genericPermissionsChecks(String authority, String exceptionMessage) {
		
		if( accessAuthorities(authority) ) {
			return true;
		}
		throw new ForbiddenException(exceptionMessage);
	}
	
	static public boolean accessAuthorities(String authority) {
		
		return SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().stream().anyMatch( auth -> auth.getAuthority().equals(authority) );
		
	}
	




}
