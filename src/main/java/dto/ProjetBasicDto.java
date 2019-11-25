package dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import entities.SrcFinancement;

public class ProjetBasicDto {

	public Integer id;
	public String intitule;
	public Double montant;
	public boolean isConvention;
	public boolean isMaitreOuvrageDel;
	public SimpleDto maitreOuvrage;
	public SimpleDto maitreOuvrageDel;
	public Integer anneeProjet;
	public boolean indh;


	
	public List<PartnerDto> partners = new ArrayList<>(); 
	
	public Integer taux;
	
	public Collection<TreeDto> localisations;
	public SimpleDto indhProgramme;
	public SimpleDto secteur;
	public SimpleDto srcFinancement;
	public SimpleDto chargeSuivi;

	public ProjetBasicDto(Integer id, String intitule, Double montant, boolean isConvention, Integer anneeProjet, 
			boolean isMaitreOuvrageDel, SimpleDto maitreOuvrage, SimpleDto maitreOuvrageDel, boolean indh) {
		this.id = id;
		this.intitule = intitule;
		this.montant = montant;
		this.isConvention = isConvention;
		this.isMaitreOuvrageDel = isMaitreOuvrageDel;
		this.maitreOuvrage = maitreOuvrage;
		this.maitreOuvrageDel = maitreOuvrageDel;
		this.indh = indh;
		this.anneeProjet = anneeProjet;
	}


	public ProjetBasicDto() {}
	
	
	
}
