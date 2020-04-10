package controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import annotations.CurrentUser;
import annotations.DeleteProjectAuth;
import annotations.EditProjectAuth;
import annotations.SaveEditedProjectAuth;
import beans.ProjetBean;
import beans.ProjetSearchBean;
import dao.DiversDao;
import dao.GenericDao;
import dao.MarcheDao;
import dao.ProjetDao;
import dao.UserDao;
import dto.DetailDto;
import dto.PageResult;
import dto.ProjetEditDto;
import entities.Projet;
import helpers.Helpers;
import services.DiversService;
import services.MarcheService;
import services.ProjetSearch;
import services.ProjetService;
import services.UserService;

@RestController
@RequestMapping(value = "/api")
public class ProjetRest {

	@Autowired
	private GenericDao<Projet, Integer> gProjetDao;
	@Autowired
	private ProjetDao projetDao;
	@Autowired
	private MarcheDao marcheDao;
	@Autowired
	private ProjetService projetService;
	@Autowired
	private ProjetSearch projetSearch;
	@Autowired
	private MarcheService marcheService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private DiversDao diversDao;
	@Autowired
	private DiversService diversService;
	@Autowired
	private UserService userService;


	
	@PostMapping(value = "/projets")
	@EditProjectAuth
	public Integer saveProjet(@CurrentUser Integer currentUser, @RequestBody ProjetBean bean) {
		
		return projetService.saveProjet(bean, currentUser);
	}
	
	@PutMapping(value = "/projets/{idProjet}")
	@SaveEditedProjectAuth
	public Integer updateProjet(@CurrentUser Integer currentUser, @PathVariable Integer idProjet, @RequestBody ProjetBean bean) {
		
		bean.idProjet = idProjet;
		return projetService.saveProjet(bean, currentUser);
	}
	
	
	
	
	@GetMapping(value = "/projets/loading/{idProjet}")
	@SaveEditedProjectAuth
	public Map<String, Object> loadingProjetEdit(@PathVariable Integer idProjet) {

		return projetService.projetLoadingForEdit(idProjet);
	}
	

	@DeleteMapping(value = "/projets/{idProjet}")
	@DeleteProjectAuth
	public void deleteProjet(@PathVariable Integer idProjet) {
		
		gProjetDao.delete(Projet.class, idProjet);
	}
	
	@GetMapping(value = "/projets/loading")
	@EditProjectAuth
	public Map<String, Object> loadingProjetNew() {
		
		return projetService.projetLoadingForEdit(null);
	}
	
	
	@GetMapping(value = "/projets/detail/{idProjet}") 
	public DetailDto getProjetForDetail(@PathVariable Integer idProjet) {
		
		return new DetailDto(
				projetService.getProjetForDetail(idProjet), 
				marcheService.getDefaultMarcheForDetail(idProjet),
				marcheDao.getMarchesIdsWithTypeByProjet(idProjet)
		);
	}
	
	@GetMapping(value = "/projets/search/loading")
	public Map<String, Object> projetsSearchLoading() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("secteurs", diversDao.getSecteurs());
		map.put("srcFinancements", diversDao.getSrcFinancements());
		map.put("communes", diversDao.getCommunes());
		map.put("acheteurs", diversDao.getAcheteurs());
		
		return map;
	}
	
	@GetMapping(value = "/projets")
	public PageResult getAllProjets(ProjetSearchBean bean) {
		
		return projetSearch.getListProjets(bean);
	}
	

	

}






