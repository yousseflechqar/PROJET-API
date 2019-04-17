package dto;

public class PartnerDto {

	
	public SimpleDto partner;
	public double montant;
	public Integer srcFinancement;
	
	
	public PartnerDto() {}
	
	public PartnerDto(SimpleDto partner, double montant, Integer srcFinancement) {
		super();
		this.partner = partner;
		this.montant = montant;
		this.srcFinancement = srcFinancement;
	}
	
	
	
}
