package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {

	
	@GetMapping(value = { "/", "/routes/**" })
	public String index() {
//		return "index";
		System.out.println("/ MAPPING /");
//		return "/reactjs/index.html";
		return "forward:/REACT-APP/index.html";
		
	}
	
	
}
