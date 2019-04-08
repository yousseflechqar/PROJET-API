package dto;

public class PartnerDto {

	
	public SimpleDto partner;
	public double montant;
	
	
	public PartnerDto() {}
	
	public PartnerDto(SimpleDto partner, double montant) {
		super();
		this.partner = partner;
		this.montant = montant;
	}
	
	
	
}
