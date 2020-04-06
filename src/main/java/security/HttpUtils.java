package security;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtils {
	
	public static HttpServletResponse constructJsonResponse(
			HttpServletResponse response, String body, Integer status
			) throws IOException {
		
		
		if(body != null) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(body);
		}
	    
		if(status != null) {			
			response.setStatus(status);
		}
	    
	    return response;
	}

}
