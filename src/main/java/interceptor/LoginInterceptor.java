package interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


import exceptions.UnauthorizedException;


public class LoginInterceptor implements HandlerInterceptor {



	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

		String uri = request.getRequestURI();
		String url = request.getServletContext().getContextPath();
		

		System.out.println("URI : " +  uri + " => " + request.getMethod());
//		System.out.println("URL : " +  request.getServletContext().getContextPath());
//		System.out.println("IP : " +  request.getRemoteAddr());
//		System.out.println("Session : " +  request.getSession() + " > " + request.getSession().getId());
//		System.out.println("getAttribute(\"user\") : " +  request.getSession().getAttribute("user"));
		
		boolean access = true;
		
		if( access ) return true;

		
		String staticUri = url+"/REACT-APP/";
		if(uri.startsWith(staticUri)) {
			return true;
		}
		
		String attachsUri = url+"/attachments/";
		if(uri.startsWith(attachsUri)) {
			return true;
		}
		
		
		if( uri.equals(url+"/") ) {
			return true;
		}
		
		if( uri.startsWith(url+"/routes/") ) {
			return true;
		}
		
		if( uri.startsWith(url+"/api/postman/") || uri.startsWith(url+"/postman/") ) {
			return true;
		}
		
		String apiUri = url+"/api/";
		if( uri.equals(apiUri+"login") || uri.equals(apiUri+"logout") ) {
			return true;
		}

		if( request.getSession().getAttribute("user") == null ) {
			System.out.println("@UnauthorizedException");
			throw new UnauthorizedException();
    	}

		return true;
		
	}
	

}
