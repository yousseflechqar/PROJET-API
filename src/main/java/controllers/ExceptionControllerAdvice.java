package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.AppException;
import exceptions.ForbiddenException;
import exceptions.UnauthorizedException;

@ControllerAdvice
public class ExceptionControllerAdvice  {
	
    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    @ResponseStatus( HttpStatus.FORBIDDEN )
    public AppException handleForbiddenException(ForbiddenException ex) {

    	ex.printStackTrace();
        return new AppException(403, ex.getMessage());
    }
    
    
//    
//    @ExceptionHandler(UnauthorizedException.class)
//    @ResponseBody
//    public AppException handleUnauthorizedException(AuthenticationException ex) {
//    	
//    	ex.printStackTrace();
//    	return new AppException(401, ex.getMessage());
//    }
	
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleException(Exception ex) throws JsonProcessingException {
//
//    	ex.printStackTrace();
//		Map<String, Object> jsonException = new HashMap<String, Object>();
//		jsonException.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
//		jsonException.put("messsage", ex.getMessage());
////		jsonException.put("time_stamp", new Date());
//
//        return new ResponseEntity<>(jsonException, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
