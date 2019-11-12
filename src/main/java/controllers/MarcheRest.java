package controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import beans.ApiValues;
import beans.AttachmentBean;
import beans.MarcheAttachs;
import beans.MarcheBean;
import dao.DiversDao;
import dao.GenericDao;
import dao.MarcheDao;
import dao.UserDao;
import dto.ProjetEditDto;
import dto.SimpleDto;
import dto.UserSession;
import entities.Marches;
import entities.Projet;
import entities.UserType;
import enums.UserRole;
import exceptions.ForbiddenException;
import helpers.Helpers;
import services.MarcheService;

@RestController
@RequestMapping(value = "/api")
public class MarcheRest {
	
	
	@Autowired
	private GenericDao<Marches, Integer> gMarcheDao;
	@Autowired
	private MarcheDao marcheDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private MarcheService marcheService;
	@Autowired
	private DiversDao diversDao;
	
	

	
	@PostMapping(value = "/marches", consumes = "multipart/form-data")
	public Integer saveMarches(
			@RequestPart("formJson") MarcheBean formJson,
			@ModelAttribute MarcheAttachs attachs
	) throws IOException, ParseException {
		
		

		return marcheService.saveMarche(formJson, attachs);
	}
	
	@GetMapping(value = "/marches/loading")
	public Map<String, Object> getMarchesData(HttpServletRequest request, HttpSession session) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		UserSession userSession = (UserSession) session.getAttribute("user");

		if(request.getParameter("edit") != null) {
			Integer idMarche = Integer.valueOf(request.getParameter("edit"));
			Helpers.checkProjetEditSecurity(userSession, userDao.getChargeSuiviByMarche(idMarche));
			map.put("marcheData", marcheService.getMarcheForEdit(idMarche));
		}

		map.put("marcheTypes", diversDao.getMarcheTypes());
		map.put("marcheEtats", diversDao.getMarcheEtats());
		map.put("osTypes", diversDao.getOsTypes());

		return map;
	}
	
//	@GetMapping(value = "/marches/edit/{idMarche}")
//	public MarcheBean getMarcheForEdit(@PathVariable Integer idMarche, HttpSession session) {
//		
//		// security check !!!
//		
//		Helpers.checkProjetEditSecurity(
//				(UserSession) session.getAttribute("user"), 
//				userDao.getChargeSuiviByMarche(idMarche)
//		);
//		
//
//		return marcheService.getMarcheForEdit(idMarche);
//
//	}
	
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
