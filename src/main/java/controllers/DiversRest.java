package controllers;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import beans.SteBean;
import dao.DiversDao;
import dao.ProjetDao;
import dto.SelectGrpDto;
import dto.SimpleDto;
import dto.TreePathDto;
import services.DiversService;

@RestController
@RequestMapping(value = "/api")
public class DiversRest {

	@Autowired
	private DiversDao diversDao;
	@Autowired
	private DiversService diversService;
	
	@GetMapping(value = "/marcheTypes")
	public List<SimpleDto> getmarcheTypes() {
		return diversDao.getMarcheTypes();
	}
	
	@PostMapping(value = "/societes")
	public Integer saveSte(@RequestBody SteBean bean) {
		return diversService.saveSte(bean);
	}
	
	@GetMapping(value = "/societes")
	public List<SimpleDto> getSocietes(HttpServletRequest request) {
		return diversDao.getSocietesByName(request.getParameter("q"));
	}
	
	@GetMapping(value = "/secteurs")
	public List<SimpleDto> getSecteurs() {
		return diversDao.getSecteurs();
	}
	
	@GetMapping(value = "/acheteurs")
	public List<SimpleDto> getAcheteurs(HttpServletRequest request) {
		
		if(request.getParameter("q") != null)
			return diversDao.getAcheteursByName(request.getParameter("q"));
		
		return diversDao.getAcheteurs();
	}
	
	
	@GetMapping(value = "/localisations")
	public Collection<TreePathDto> getCommunesWithFractions() {
		return diversService.getCommunesWithFractions();
	}
	
	@GetMapping(value = "/communes")
	public List<SimpleDto> getCommunes() {
		return diversDao.getCommunes();
	}
	
	@GetMapping(value = "/srcFinancements")
	public List<SimpleDto> getSrcFinancements() {
		return diversDao.getSrcFinancements();
	}
	
	@GetMapping(value = "/financements/{maitreOuvrage}")
	public List<SimpleDto> getFinancements(@PathVariable Integer maitreOuvrage) {
		return diversDao.getFinancements(maitreOuvrage);
	}
	
	@GetMapping(value = "/parent/programmes")
	public Collection<SelectGrpDto> getParentProgrammesWithPhases() {
		return diversService.getParentProgrammesWithPhases();
	}
	
	@GetMapping(value = "/getProgrammesWithPhases")
	public Collection<SelectGrpDto> getProgrammesWithPhases() {
		return diversService.getProgrammesWithPhases2();
	}
	
}
