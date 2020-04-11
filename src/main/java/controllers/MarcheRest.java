package controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import beans.MarcheAttachs;
import beans.MarcheBean;
import dao.GenericDao;
import entities.Marches;
import security.annotations.CurrentUser;
import security.annotations.DeleteProjectAuth;
import security.annotations.EditProjectAuth;
import security.annotations.SaveEditedMarcheAuth;
import security.annotations.SaveEditedProjectAuth;
import services.MarcheService;

@RestController
@RequestMapping("/api")
public class MarcheRest {
	
	
	@Autowired
	private GenericDao<Marches, Integer> gMarcheDao;
	@Autowired
	private MarcheService marcheService;

	@GetMapping(value = "/projets/{idProjet}/marches/loading")
	@SaveEditedProjectAuth
	public Map<String, Object> loadingMarcheNew(@PathVariable Integer idProjet) {
		
		return marcheService.marcheLoadingForEdit(null);
	}
	
	@GetMapping(value = "/marches/loading/{idMarche}")
	@SaveEditedMarcheAuth
	public Map<String, Object> loadingMarcheEdit(@PathVariable Integer idMarche) {
		
		return marcheService.marcheLoadingForEdit(idMarche);
	}

	/**
	 * @SaveEditedProjectAuth on doit verifier if the current user is chargé de suivi du projet 
	 * avant d'ajouter le marché en questions
	 */
	@PostMapping(value = "/projets/{idProjet}/marches", consumes = "multipart/form-data")
	@SaveEditedProjectAuth
	public Integer saveNewMarche(
			@CurrentUser Integer currentUser,
			@PathVariable Integer idProjet,
			@RequestPart("formJson") MarcheBean marcheBean,
			@ModelAttribute MarcheAttachs attachs
	) throws IOException, ParseException {

		return marcheService.saveMarche(marcheBean, attachs, currentUser);
	}
	
	@PutMapping(value = "/marches/{idMarche}", consumes = "multipart/form-data")
	@SaveEditedMarcheAuth
	public Integer updateMarche(
			@CurrentUser Integer currentUser, 
			@PathVariable Integer idProjet,
			@PathVariable Integer idMarche,
			@RequestPart("formJson") MarcheBean marcheBean,
			@ModelAttribute MarcheAttachs attachs
			) throws IOException, ParseException {
		
		marcheBean.idMarche = idMarche;
		return marcheService.saveMarche(marcheBean, attachs, currentUser);
	}
	

	
	@DeleteMapping(value = "/marches/{idMarche}")
	@DeleteProjectAuth
	public void deleteMarche(@PathVariable Integer idMarche) {
		
		gMarcheDao.delete(Marches.class, idMarche);
	}
	
	@GetMapping(value = "/marches/default/{idProjet}")
	public MarcheBean getDefaultMarcheForDetail(@PathVariable Integer idProjet) {
		
		return marcheService.getDefaultMarcheForDetail(idProjet);
	}
	
	@GetMapping(value = "/marches/detail/{idMarche}")
	public MarcheBean getMarcheForDetail(@PathVariable Integer idMarche) {
		
		return marcheService.getMarcheForDetail(idMarche);
	}
	


}
