package beans;

public class LocalisationBean {

	
	public Integer 	idCommune;
	public String 	commune;
	public Integer 	idFraction;
	public String 	fraction;
	

	
	
	

	public LocalisationBean() {}
	
	
	public LocalisationBean(String commune, String fraction) {
		super();
		this.commune = commune;
		this.fraction = fraction;
	}
	
	
	
	
	public LocalisationBean(Integer idCommune, String commune, Integer idFraction, String fraction) {
		super();
		this.idCommune = idCommune;
		this.commune = commune;
		this.idFraction = idFraction;
		this.fraction = fraction;
	}


	public String getCommune() {
		return commune;
	}
	public void setCommune(String commune) {
		this.commune = commune;
	}
	public Integer getIdCommune() {
		return idCommune;
	}
	public void setIdCommune(Integer idCommune) {
		this.idCommune = idCommune;
	}
	public Integer getIdFraction() {
		return idFraction;
	}
	public void setIdFraction(Integer idFraction) {
		this.idFraction = idFraction;
	}

	public String getFraction() {
		return fraction;
	}
	public void setFraction(String fraction) {
		this.fraction = fraction;
	}
	
	
	
	
}
