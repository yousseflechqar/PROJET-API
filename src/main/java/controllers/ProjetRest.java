package controllers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import beans.LocalisationBean;
import beans.ProjetBean;
import beans.SimpleBean;
import dao.GenericDao;
import dao.ProjetDao;
import dto.ProjetDto;
import dto.ProjetEditDto;
import dto.SimpleDto;
import dto.TreeDto;
import entities.Projet;
import services.ProjetService;

@RestController
@RequestMapping(value = "/api")
public class ProjetRest {

	@Autowired
	private GenericDao<Projet, Integer> genericDao;
	@Autowired
	private ProjetDao projetDao;
	@Autowired
	private ProjetService projetService;

	@PostMapping(value = "/projets")
	public ProjetBean saveProjet(@RequestBody ProjetBean bean) {

		projetService.saveProjet(bean);

		return bean;
	}
	
	@GetMapping(value = "/projets/edit/{idProjet}")
	public ProjetEditDto getProjetForEdit(@PathVariable Integer idProjet) {
		
		return projetService.getProjetForEdit(idProjet);
	}
	
	@GetMapping(value = "/projets")
	public List<ProjetDto> getAllProjets() {
		
		return projetDao.getListProjets();
	}
	
	@DeleteMapping(value = "/projets/{idProjet}")
	public void deleteProjet(@PathVariable Integer idProjet) {
		
		 genericDao.delete(Projet.class, idProjet);
	}
	
	@DeleteMapping(value = "/projets")
	public void deleteAllProjets() {
		
		genericDao.deleteAll(Projet.class);
	}
	
	@GetMapping(value = "/secteurs")
	public List<SimpleDto> getSecteurs() {
		return projetDao.getSecteurs();
	}
	
	@GetMapping(value = "/acheteurs")
	public List<SimpleDto> getAcheteurs(HttpServletRequest request) {
		return projetDao.getAcheteursByName(request.getParameter("q"));
	}
	
	@GetMapping(value = "/partners")
	public List<SimpleDto> getPartnersByName(HttpServletRequest request) {
		return projetDao.getPartnersByName(request.getParameter("q"));
	}
	
	@GetMapping(value = "/localisations")
	public Collection<TreeDto> getCommunesWithFractions() {
		return projetService.getCommunesWithFractions();
	}
}






