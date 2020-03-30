package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beans.LocalisationBean;
import beans.ProgrammeBean;
import beans.SteBean;
import dao.DiversDao;
import dao.GenericDao;
import dto.SelectGrpDto;
import dto.SimpleDto;
import dto.TreePathDto;
import entities.ProjetMaitreOuvrage;
import entities.Responsable;
import entities.Societe;

@Service
public class DiversService {

	
	private @Autowired DiversDao diversDao;
	
	private @Autowired GenericDao<Societe, Integer> gSocieteDao;
	
	private @Autowired GenericDao<Responsable, Integer> gResponsableDao;
	
	public Collection<TreePathDto> getCommunesWithFractions() {
		
		List<LocalisationBean> communes = diversDao.getCommunesWithFractions();
		
		Map<Integer, TreePathDto> communetree = new LinkedHashMap<Integer, TreePathDto>();
		communes.forEach((com) -> {
            if (!communetree.containsKey(com.idCommune)){
            	communetree.put(com.idCommune, new TreePathDto(com.idCommune, com.commune, String.valueOf(com.idCommune)));
            }
            communetree.get(com.idCommune).children.add(new TreePathDto(com.idFraction, com.fraction, com.idCommune+"."+com.idFraction));
		});
		
		return communetree.values();
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<SelectGrpDto> getProgrammesWithPhases2() {
		
		List<ProgrammeBean> programmes = diversDao.getIndhProgrammes2();
		Map<Integer, List<SimpleDto>> mapProgWithLeafs = new LinkedHashMap<Integer, List<SimpleDto>>();
		Map<Integer, SelectGrpDto> selectGrpMapPhase = new LinkedHashMap<Integer, SelectGrpDto>();
	
		programmes.forEach((prog) -> {
			
			if(prog.idParent != null) {
				if(!mapProgWithLeafs.containsKey(prog.idParent))
					mapProgWithLeafs.put(prog.idParent, new ArrayList<SimpleDto>());
				mapProgWithLeafs.get(prog.idParent).add(new SimpleDto(prog.id, prog.label));
			} 

		});
		
		
		programmes.forEach((prog) -> {
			
			if( prog.idParent == null ) {
				
				if (!selectGrpMapPhase.containsKey(prog.phase)){
					selectGrpMapPhase.put(prog.phase, new SelectGrpDto("Phase " + 
							String.join("", Collections.nCopies(prog.phase, "I"))));
				}
				
				if(!mapProgWithLeafs.containsKey(prog.id)) {
					selectGrpMapPhase.get(prog.phase).addOption(new SimpleDto(prog.id, prog.label));
				} else {
					selectGrpMapPhase.get(prog.phase).addOption(new SelectGrpDto(prog.label, mapProgWithLeafs.get(prog.id)));
				}
				
			}

	
			
		});
	

		
		return selectGrpMapPhase.values();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<SelectGrpDto> getParentProgrammesWithPhases() {
		
		List<ProgrammeBean> programmes = diversDao.getParentProgrammes();

		Map<Integer, SelectGrpDto> selectGrpMapPhase = new LinkedHashMap<Integer, SelectGrpDto>();

		programmes.forEach((prog) -> {

			if (!selectGrpMapPhase.containsKey(prog.phase)){
				selectGrpMapPhase.put(prog.phase, new SelectGrpDto("Phase " + 
						String.join("", Collections.nCopies(prog.phase, "I"))));
			}
			
			selectGrpMapPhase.get(prog.phase).options.add(new SimpleDto(prog.id, prog.label));

		});
		
		
		
		return selectGrpMapPhase.values();
	}
	
	
	@Transactional(rollbackOn = Exception.class)
	public Integer saveSte(SteBean bean) {
		
		boolean editMode = bean.idSte != null;
		
		Societe ste = new Societe();
		Responsable rsp = new Responsable();
		
		if(editMode) {
			ste = gSocieteDao.find(bean.idSte, Societe.class);
			rsp = ste.getResponsable();
		}
		
		ste.setNom(bean.name);
		ste.setAdresse(bean.location);
		
		rsp.setNom(bean.responsable);
		rsp.setEmail(bean.email);
		rsp.setPhones(bean.phones);

		if(!editMode) {
			ste.setResponsable(gResponsableDao.persist(rsp));
			gSocieteDao.persist(ste);
		}
		
		
		return ste.getId();
		
	}
}
