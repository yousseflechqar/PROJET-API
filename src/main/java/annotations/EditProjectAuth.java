package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;


import static security.SecurityUtils.editProjectPermissionsChecks;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
//@PreAuthorize("hasAuthority(T(enums.PermissionEnum).EDIT_PROJECT.getKey())")
@PreAuthorize("@securityUtils.editProjectPermissionsChecks()")
public @interface EditProjectAuth {}