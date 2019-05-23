package dto;

public class PartnerDto {

	
	public SimpleDto partner;
	public Double montant;

	
	
	public PartnerDto() {}
	
	public PartnerDto(SimpleDto partner, Double montant) {
		super();
		this.partner = partner;
		this.montant = montant;
	}
	
	
	
}
