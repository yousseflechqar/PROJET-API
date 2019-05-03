package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dao.MarcheDiversDao;
import dto.SimpleDto;

@RestController
@RequestMapping(value = "/api")
public class MarcheDiversRest {
	
	@Autowired
	MarcheDiversDao marcheDiversDao;
	
	@GetMapping(value = "/osTypes")
	public List<SimpleDto> saveSte() {
		return marcheDiversDao.getOsTypes();
	}
	
	@GetMapping(value = "/marcheEtats")
	public List<SimpleDto> getMarcheEtats() {
		return marcheDiversDao.getMarcheEtats();
	}
	

}
