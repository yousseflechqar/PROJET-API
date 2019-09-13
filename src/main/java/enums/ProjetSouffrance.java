package enums;

public enum ProjetSouffrance {

	
	EN_ARRET(1), EN_RETARD(2), DELAI_DEPASSE(3); 

    public Integer value ;  
    
    private ProjetSouffrance(Integer value) {  
        this.value = value ;  
    } 
    
}
