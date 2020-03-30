package controllers;




import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value="/postman")
public class PostmanRest {
	
	
	public String postman() {
		return "index";
	}
	
//	@GetMapping
//	public String postmanGetMapping() {
//		return "@GetMapping";
//	}
	
	
	@GetMapping(value = "/1")
	public String postman1() {
		return "postman1";
	}

}
