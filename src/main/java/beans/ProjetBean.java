package beans;

import java.util.List;
import java.util.Map;

import dto.SimpleDto;

public class ProjetBean {
	
	public Integer idProjet;
	public String intitule;
	public Double montant;
	public boolean isConvention = false;
	
	public boolean indh = false;
	public boolean prdts = false;
	public Integer indhProgramme;
	
	public List<String> localisations;
	public List<String> partners;
	
	public Integer secteur;
	public String maitreOuvrage;
	public boolean isMaitreOuvrageDel = false;
	public Integer maitreOuvrageDel;
	
	public Integer chargeSuivi;
	
	public Integer srcFinancement;

}
