package dto;

import java.util.ArrayList;
import java.util.List;

import entities.Acheteur;
import entities.ProjetIndh;
import entities.ProjetMaitreOuvrage;
import entities.SrcFinancement;
import entities.User;


public class ProjetEditDto {

	public String intitule;
	public Double montant;
	public boolean isConvention;
	public boolean isMaitreOuvrageDel;
	public SimpleDto maitreOuvrage;
	public SimpleDto maitreOuvrageDel;
	public boolean indh;
	public boolean prdts;
	
	
	public Integer indhProgramme;
	public Integer anneeProjet;
	public Integer secteur;
	public Integer srcFinancement;
	public Integer chargeSuivi;
	
	public List<PartnerDto> partners = new ArrayList<PartnerDto>(); 
	public List<String> localisations = new ArrayList<String>(); 
	
	

	public ProjetEditDto() {}
	
	public ProjetEditDto(String intitule, Double montant, SrcFinancement srcFinancement, boolean isConvention,
			ProjetMaitreOuvrage pMo, ProjetMaitreOuvrage pMod, 
			Integer secteur, User chargeSuivi, Integer anneeProjet,
			ProjetIndh indh, boolean prdts) {
		super();
		this.intitule = intitule;
		this.montant = montant;
		this.srcFinancement = (srcFinancement != null) ? srcFinancement.getId() : null;
		this.isConvention = isConvention;
		this.maitreOuvrage = new SimpleDto(pMo.getMaitreOuvrage().getId(), pMo.getMaitreOuvrage().getNom());
		this.isMaitreOuvrageDel = (pMod != null);
		this.maitreOuvrageDel = (pMod != null) ? new SimpleDto(pMod.getMaitreOuvrage().getId(), pMod.getMaitreOuvrage().getNom()) : null;
		this.secteur = secteur;
		this.chargeSuivi = chargeSuivi != null ? chargeSuivi.getId() : null;
		this.indh = (indh != null);
		this.indhProgramme = (indh != null) ? indh.getProgramme().getId() : null;
		this.prdts = prdts;
		this.anneeProjet = anneeProjet;
	}
	
	
	
	
}
