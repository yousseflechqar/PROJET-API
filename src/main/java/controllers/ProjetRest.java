package controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import beans.ProjetSearchBean;
import beans.SimpleBean;
import dao.DiversDao;
import dao.GenericDao;
import dao.MarcheDao;
import dao.ProjetDao;
import dao.SearchProjetDao;
import dao.UserDao;
import dto.DetailDto;
import dto.PageResult;
import dto.ProjetBasicDto;
import dto.ProjetDto;
import dto.ProjetEditDto;
import dto.SelectGrpDto;
import dto.SimpleDto;
import dto.TreeDto;
import dto.TreePathDto;
import dto.UserSession;
import entities.Marches;
import entities.Projet;
import exceptions.ForbiddenException;
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
	public Integer saveProjet(@RequestBody ProjetBean bean, HttpSession session) {

		UserSession userSession = (UserSession) session.getAttribute("user");
		
		return projetService.saveProjet(bean, userSession);
	}
	
	@GetMapping(value = "/projets/loading")
	public Map<String, Object> getProjetData(HttpServletRequest request, HttpSession session) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		UserSession userSession = (UserSession) session.getAttribute("user");

		
		if(request.getParameter("edit") != null) {
			
			Integer idProjet = Integer.valueOf(request.getParameter("edit"));
			
			Helpers.checkProjetEditSecurity(userSession, userDao.getChargeSuiviByProj(idProjet));
			
			ProjetEditDto proj = projetService.getProjetForEdit(idProjet);
			map.put("projetData", proj);
			
			if(proj.srcFinancement != null && proj.srcFinancement.equals(enums.SrcFinancement.INDH.val())) {
				map.put("indhProgrammes", diversService.getParentProgrammesWithPhases());
			}
		}
		
		
		
		map.put("secteurs", diversDao.getSecteurs());
		map.put("localisations", diversService.getCommunesWithFractions());
		map.put("srcFinancements", diversDao.getSrcFinancements());
		
		if( Helpers.canUserAssign(userSession)) {			
			map.put("chargesSuivi", userService.getChargesSuivi2());
		}
		

		
		return map;
	}
	
	@GetMapping(value = "/projets/edit/{idProjet}")
	public ProjetEditDto getProjetForEdit(@PathVariable Integer idProjet, HttpSession session) {
		
		
		Helpers.checkProjetEditSecurity(
				(UserSession) session.getAttribute("user"), 
				userDao.getChargeSuiviByProj(idProjet)
		);
		
		return projetService.getProjetForEdit(idProjet);
	}
	
	@GetMapping(value = "/projets/detail/{idProjet}") 
	public DetailDto getProjetForDetail(@PathVariable Integer idProjet) {
		
		return new DetailDto(
				projetService.getProjetForDetail(idProjet), 
				marcheService.getDefaultMarcheForDetail(idProjet),
				marcheDao.getMarchesIdsWithTypeByProjet(idProjet)
		);
	}
	
	@GetMapping(value = "/projets")
	public PageResult getAllProjets(ProjetSearchBean bean) {
		
		return projetSearch.getListProjets(bean);
	}
	

	@DeleteMapping(value = "/projets/{idProjet}")
	public void deleteProjet(@PathVariable Integer idProjet) {
		
		gProjetDao.delete(Projet.class, idProjet);
	}
	
	@DeleteMapping(value = "/projets/all")
	public void deleteAllProjets() {
		
		gProjetDao.deleteAll(Projet.class);
	}
	

	

}






