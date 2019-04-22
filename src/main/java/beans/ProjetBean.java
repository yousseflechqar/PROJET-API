package beans;

import java.util.Arrays;
import java.util.List;

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

}
