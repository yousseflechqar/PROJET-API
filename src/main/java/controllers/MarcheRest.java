package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
	
	
//	@PostMapping(value = "/marches")
//	public Integer saveMarches(@RequestBody MarcheBean bean) throws IOException {
//		return marcheService.saveMarche(bean);
//	}
	
//	@PostMapping(value = "/marches")
//	public Integer saveMarches(MarcheForm marcheForm) throws IOException {
//
//		return marcheService.saveMarche(marcheForm.formJson, marcheForm.attachOs);
//	}
	@PostMapping(value = "/marches", consumes = "multipart/form-data")
	public Integer saveMarches(
			@RequestPart("formJson") MarcheBean formJson,
			@ModelAttribute MarcheAttachs attachs
	) throws IOException {
		
		

		return marcheService.saveMarche(formJson, attachs);
	}
	
	
//	@PostMapping(value = "/postman/marches")
//	public String savePostmanMarches(@ModelAttribute MarcheBean bean) throws IOException {
//		return bean.attachment.getOriginalFilename();
//	}
//	@PostMapping(value = "/postman/marches3")
//	public String savePostmanMarches3(@ModelAttribute("apiValues") MarcheBean bean) throws IOException {
//		return bean.attachment.getOriginalFilename();
//	}
//	@PostMapping(value = "/postman/marches2")
//	public String savePostmanMarches2(@RequestPart("apiValues") MarcheBean bean) throws IOException {
//		return bean.attachment.getOriginalFilename();
//	}
//	
//	@PostMapping(value = "/postman/marches22")
//	public String savePostmanMarches22(ApiValues bean) throws IOException {
//		return bean.apiValues.attachment.getOriginalFilename();
//	}
//	
//	
//	@PostMapping(value = "/postman/marches33")
//	public String savePostmanMarches33(@ModelAttribute ApiValues bean) throws IOException {
//		return bean.apiValues.attachment.getOriginalFilename();
//	}
	



	
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
