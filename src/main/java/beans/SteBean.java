package beans;

public class SteBean {
	
	public Integer idSte;
	public String name;
	public String location;
	public String responsable;
	public String email;
	public String phones;
	
	
	public SteBean() {}
	
	public SteBean(Integer idSte, String name, String location, String responsable, String email, String phones) {
		this.idSte = idSte;
		this.name = name;
		this.location = location;
		this.responsable = responsable;
		this.email = email;
		this.phones = phones;
	}
	
	

}
