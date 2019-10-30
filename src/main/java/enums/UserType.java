package enums;

public enum UserType {

	
	utilisateur(1), administrateur(2), invite(3);
	
	
	public Integer type;
	
	private UserType(Integer type) {
		this.type = type;
	}
	
	
    
    public Integer type() {
    	return type;
    }

}
