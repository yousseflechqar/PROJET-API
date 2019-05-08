package dto;

public class PartnerDto {

	
	public SimpleDto partner;
	public Double montant;
	public SimpleDto srcFinancement;
	
	
	public PartnerDto() {}
	
	public PartnerDto(SimpleDto partner, Double montant, SimpleDto srcFinancement) {
		super();
		this.partner = partner;
		this.montant = montant;
		this.srcFinancement = srcFinancement;
	}
	
	
	
}
