package security;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.AppException;

public class HttpUtils {
	
	public static HttpServletResponse jsonExceptionResponse(
			HttpServletResponse response, Exception exception, Integer status
			) throws JsonGenerationException, JsonMappingException, IOException  {

//		new ObjectMapper().writeValue(response.getOutputStream(), new AppException(status, exception.getLocalizedMessage()));
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new ObjectMapper().writeValueAsString(new AppException(status, exception.getLocalizedMessage())));
		response.setStatus(status);
	    return response;
	}
	
	
	public static HttpServletResponse jsonResponse(HttpServletResponse response, Object obj) 
			throws JsonGenerationException, JsonMappingException, IOException  {

		response.getWriter().write(new ObjectMapper().writeValueAsString(obj));
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(200);
		return response;
	}

}
