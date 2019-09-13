//package interceptor;
//
//
//import java.util.Arrays;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import annotations.Permitted;
//import dao.ProjetDao;
//import enums.RoleEnum;
//import exceptions.ForbiddenAjaxException;
//import exceptions.ForbiddenException;
//import utils.ProjectsUtils;
//
//
//@Component
//@Aspect
//public class PermissionAspect {
//	
//	@Autowired
//	private ProjetDao projetDao;
//	
//	@SuppressWarnings("unchecked")
//	@Around("@annotation(annotations.Permitted)")
//    public Object profilingPermission (ProceedingJoinPoint pjp) throws Throwable {
//
//		
//	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//	String uri = request.getRequestURI();
//
//	if( uri.contains("/ajax/") || "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) )
//		return pjp.proceed();
//			
//	if( ! ProjectsUtils.checkUserRole
//			   ( 
//				   (List<Integer>) request.getSession().getAttribute("roles_") , 
//				   ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(Permitted.class).value().val 
//			   ) ){
//
//		  	if( "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ){
//		  		
//		  		System.out.println("@ForbiddenAjaxException");
//		  		throw new ForbiddenAjaxException();
//		  	}
//		  	
//		  	
//		  	System.out.println("@ForbiddenException");
//		  	
//			throw new ForbiddenException();
//		}
//
//		return pjp.proceed();
//
//   }
//	
//
//   
//   
//
//}