package beans;

public class ProjetSearchBean {
	
	public String intitule;
	public Integer secteur;
	public Integer maitreOuvrage;
	public Integer commune;
	
	
	
	
	public ProjetSearchBean() {}
	public ProjetSearchBean(String intitule, Integer secteur, Integer maitreOuvrage, Integer commune) {
		super();
		this.intitule = intitule;
		this.secteur = secteur;
		this.maitreOuvrage = maitreOuvrage;
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
	public Integer getMaitreOuvrage() {
		return maitreOuvrage;
	}
	public void setMaitreOuvrage(Integer maitreOuvrage) {
		this.maitreOuvrage = maitreOuvrage;
	}
	public Integer getCommune() {
		return commune;
	}
	public void setCommune(Integer commune) {
		this.commune = commune;
	}
	
	
	

}
