package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import helpers.Helpers;


@Controller
public class IndexController {

	
	@GetMapping(value = { "/", "/routes/**" })
	public String index() {

		return "forward:/REACT-APP/index.html";
	}
	
	
	@GetMapping(value = "/attachments/{marche}/{attachType}/download")
	public @ResponseBody void getFile(
								@RequestParam("n") String fileName, 
								@RequestParam("d") String date, 
								@PathVariable Integer marche, 
								@PathVariable String attachType, 
			HttpServletResponse response) throws IOException {
		
		System.out.println("DOWNLOADING ...");

		response.setHeader("Content-Disposition", "Attachment ; filename="+ fileName );
		
		InputStream in = Files.newInputStream(Paths.get(Helpers.getPathDate(marche, date, attachType) + "/" + fileName));
		ServletOutputStream out = response.getOutputStream();
		
		IOUtils.copy(in, response.getOutputStream());
		
		out.flush();
		in.close();

	}
	
}
