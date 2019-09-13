package enums;

public enum RoleEnum {
	
	
	ajouter_projet(1), supprimer_projet(2), affecter_project(3), valider_project(4), 
	edit_users(10), edit_localisation(11), ajouter_acheteur(12),
	gestion_convention(20), 
	gestion_all_divisions(30), 
	consultation(100); 

    public Integer val ;  
    
    private RoleEnum(Integer val) {  
        this.val = val ;  
    } 


}
