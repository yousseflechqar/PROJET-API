package controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import beans.MarcheBean;
import dao.GenericDao;
import dao.MarcheDao;
import dto.ProjetEditDto;
import dto.SimpleDto;
import entities.Marches;
import entities.Projet;
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
	public Integer saveMarches(@RequestBody MarcheBean bean) throws IOException {
		return marcheService.saveMarche(bean);
	}
	
	@GetMapping(value = "/marches/edit/{idMarche}")
	public MarcheBean getMarcheForEdit(@PathVariable Integer idMarche) {
		
		return marcheService.getMarcheForEdit(idMarche);
	}
	
	@GetMapping(value = "/marches/default/{idProjet}")
	public MarcheBean getDefaultMarcheForDetail(@PathVariable Integer idProjet) {
		
		return marcheService.getDefaultMarcheForDetail(idProjet);
	}
	
	@GetMapping(value = "/marches/detail/{idMarche}")
	public MarcheBean getMarcheForDetail(@PathVariable Integer idMarche) {
		
		return marcheService.getMarcheForDetail(idMarche);
	}
	
	@DeleteMapping(value = "/marches/{idMarche}")
	public void deleteMarche(@PathVariable Integer idMarche) {
		
		gMarcheDao.delete(Marches.class, idMarche);
	}

}
