package enums;

public enum UserRole {
	
	

	saisir_projet(1), affecter_projet(2), controler_projet(3), valider_projet(4), supprimer_projet(5), 
	gestion_utilisateurs(100); 

    public Integer role ;  
    
    private UserRole(Integer role) {  
        this.role = role ;  
    } 

    
    public Integer role() {
    	return role;
    }

}
