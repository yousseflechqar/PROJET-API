package dto;

public class PartnerDto {

	
	public SimpleDto partner;
	public double montant;
	public SimpleDto srcFinancement;
	
	
	public PartnerDto() {}
	
	public PartnerDto(SimpleDto partner, double montant, SimpleDto srcFinancement) {
		super();
		this.partner = partner;
		this.montant = montant;
		this.srcFinancement = srcFinancement;
	}
	
	
	
}
