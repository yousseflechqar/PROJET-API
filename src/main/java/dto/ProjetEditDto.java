package dto;

import java.util.ArrayList;
import java.util.List;

public class ProjetEditDto {

	public String intitule;
	public double montant;
	
	public boolean isConvention;
	
	public boolean isMaitreOuvrageDel;
	public SimpleDto maitreOuvrage;
	public Integer srcFinancement;
	public SimpleDto maitreOuvrageDel;
	
	public boolean indh;
	public boolean prdts;
	public Integer indhProgramme;
	
	public List<PartnerDto> partners = new ArrayList<PartnerDto>(); 
	public List<String> localisations = new ArrayList<String>(); 
	
	public Integer secteur;

	public ProjetEditDto() {}
	
	public ProjetEditDto(String intitule, double montant, boolean isConvention,
			SimpleDto maitreOuvrage, Integer srcFinancement, boolean isMaitreOuvrageDel, SimpleDto maitreOuvrageDel, Integer secteur,
			boolean indh, Integer indhProgramme, boolean prdts) {
		super();
		this.intitule = intitule;
		this.montant = montant;
		this.isConvention = isConvention;
		this.isMaitreOuvrageDel = isMaitreOuvrageDel;
		this.maitreOuvrage = maitreOuvrage;
		this.srcFinancement = srcFinancement;
		this.maitreOuvrageDel = maitreOuvrageDel;
		this.secteur = secteur;
		this.indh = indh;
		this.prdts = prdts;
		this.indhProgramme = indhProgramme;
	}
	
	
	
	
}
