package beans;

import java.util.Arrays;
import java.util.List;

public class ProjetBean {
	
	public Integer idProjet;
	public String intitule;
	public Double montant;
	public Boolean isConvention = false;
	
	
	public List<String> localisations;
	public List<String> partners;
	
	public Integer secteur;
	public Integer maitreOuvrage;
	public Boolean isMaitreOuvrageDel = false;
	public Integer maitreOuvrageDel;

}
