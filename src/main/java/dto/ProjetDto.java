package dto;

import java.util.ArrayList;
import java.util.List;

public class ProjetDto {
	
	public Integer id;
	public String intitule;
	public Integer taux;
	
	public String maitreOuvrage;
	
	public Integer communeId;
	public String communeLabel;
	public List<String> localisations = new ArrayList<String>();


	public ProjetDto(Integer id, String intitule, Integer taux, String maitreOuvrage, Integer communeId, String communeLabel) {
		this.id = id;
		this.intitule = intitule;
		this.taux = taux;
		this.maitreOuvrage = maitreOuvrage;
		this.communeId = communeId;
		this.communeLabel = communeLabel;
	}
	
	
	

}
