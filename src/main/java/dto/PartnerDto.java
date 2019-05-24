package dto;

public class PartnerDto {

	
	public SimpleDto partner;
	public Double montant;
	public Integer contribution;
	public String commentaire;
	
	public PartnerDto() {}
	

	
	public PartnerDto(SimpleDto partner, Double montant, Integer contribution, String commentaire) {
		super();
		this.partner = partner;
		this.montant = montant;
		this.contribution = contribution;
		this.commentaire = commentaire;
	}
	
	
	
}
