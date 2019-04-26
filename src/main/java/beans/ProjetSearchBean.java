package beans;

public class ProjetSearchBean {
	
	public String intitule;
	public Integer secteur;
	public Integer acheteur;
	public Integer commune;
	public Integer acheteurType;
	public Integer srcFinancement;
	
	
	
	
	public ProjetSearchBean() {}
	public ProjetSearchBean(String intitule, Integer secteur, Integer acheteur, Integer commune) {
		super();
		this.intitule = intitule;
		this.secteur = secteur;
		this.acheteur = acheteur;
		this.commune = commune;
	}
	public String getIntitule() {
		return intitule;
	}
	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}
	public Integer getSecteur() {
		return secteur;
	}
	public void setSecteur(Integer secteur) {
		this.secteur = secteur;
	}
	public Integer getAcheteur() {
		return acheteur;
	}
	public void setAcheteur(Integer acheteur) {
		this.acheteur = acheteur;
	}
	public Integer getCommune() {
		return commune;
	}
	public void setCommune(Integer commune) {
		this.commune = commune;
	}
	public Integer getAcheteurType() {
		return acheteurType;
	}
	public void setAcheteurType(Integer acheteurType) {
		this.acheteurType = acheteurType;
	}
	public Integer getSrcFinancement() {
		return srcFinancement;
	}
	public void setSrcFinancement(Integer srcFinancement) {
		this.srcFinancement = srcFinancement;
	}

	
	
	

}
