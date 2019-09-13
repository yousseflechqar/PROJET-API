package dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjetBasicDto<TL, T> {

	
	public String intitule;
	public Double montant;
	public boolean isConvention;
	public boolean isMaitreOuvrageDel;
	public SimpleDto maitreOuvrage;
	public SimpleDto maitreOuvrageDel;
	public boolean indh;
	public boolean prdts;
	
	public List<PartnerDto> partners = new ArrayList<>(); 
	
	public Integer taux;
	
	public Collection<TL> localisations;
	public T indhProgramme;
	public T secteur;
	public T srcFinancement;

	public ProjetBasicDto(String intitule, Double montant, boolean isConvention, boolean isMaitreOuvrageDel,
			SimpleDto maitreOuvrage, SimpleDto maitreOuvrageDel, boolean indh, boolean prdts) {
		this.intitule = intitule;
		this.montant = montant;
		this.isConvention = isConvention;
		this.isMaitreOuvrageDel = isMaitreOuvrageDel;
		this.maitreOuvrage = maitreOuvrage;
		this.maitreOuvrageDel = maitreOuvrageDel;
		this.indh = indh;
		this.prdts = prdts;
	}


	public ProjetBasicDto() {}
	
	
	
}
