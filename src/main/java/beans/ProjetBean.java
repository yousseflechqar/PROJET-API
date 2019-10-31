package beans;

import java.util.List;
import java.util.Map;

import dto.PartnerDto;
import dto.SimpleDto;

public class ProjetBean {
	
	public Integer idProjet;
	public String intitule;
	public Double montant;
	public boolean isConvention = false;
//	public boolean indh = false;
//	public boolean prdts = false;
	public Integer indhProgramme;
	
	public List<String> localisations;
	public List<PartnerDto> partners;
	
	public Integer secteur;
	public Integer maitreOuvrage;
	public boolean isMaitreOuvrageDel = false;
	public Integer maitreOuvrageDel;
	public Integer chargeSuivi;
	public Integer anneeProjet;
	public Integer srcFinancement;
}
