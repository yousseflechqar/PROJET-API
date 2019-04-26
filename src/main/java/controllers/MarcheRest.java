package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import beans.MarcheBean;
import dao.GenericDao;
import dao.MarcheDao;
import entities.Marches;
import services.MarcheService;

@RestController
@RequestMapping(value = "/api")
public class MarcheRest {
	
	
	@Autowired
	private GenericDao<Marches, Integer> gMarcheDao;
	@Autowired
	private MarcheDao marcheDao;
	@Autowired
	private MarcheService marcheService;
	
	
	@PostMapping(value = "/marches")
	public Integer saveMarches(@RequestBody MarcheBean bean) {

		

		return marcheService.saveMarche(bean);
	}

}
