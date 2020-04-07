package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class ExceptionControllerAdvice  {
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) throws JsonProcessingException {

    	
		Map<String, Object> jsonException = new HashMap<String, Object>();
		jsonException.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
		jsonException.put("messsage", ex.getMessage());
//		jsonException.put("time_stamp", new Date());

        return new ResponseEntity<>(jsonException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
