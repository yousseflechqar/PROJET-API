package interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


import exceptions.UnauthorizedException;


public class LoginInterceptor extends HandlerInterceptorAdapter {



	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

		String uri = request.getRequestURI();
		String url = request.getServletContext().getContextPath();
		
		System.out.println("URI : " +  uri);
//		System.out.println("URL : " +  request.getServletContext().getContextPath());
//		System.out.println("IP : " +  request.getRemoteAddr());
//		System.out.println("Session : " +  request.getSession() + " > " + request.getSession().getId());
//		System.out.println("getAttribute(\"user\") : " +  request.getSession().getAttribute("user"));
		

		
		String staticUri = url+"/REACT-APP/";
		if(uri.startsWith(staticUri)) {
			return true;
		}
		
		
		if( uri.equals(url+"/") ) {
			return true;
		}
		
		if( uri.startsWith(url+"/routes/") ) {
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
