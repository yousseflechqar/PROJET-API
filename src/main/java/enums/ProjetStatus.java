package enums;

public enum ProjetStatus {

	
	EN_ARRET(1), EN_RETARD(2), DELAI_DEPASSE(3), EN_COURS(4), ACHEVE(5), RESILIE(6);
    public Integer value ;  
    
    private ProjetStatus(Integer value) {  
        this.value = value ;  
    } 
    
    public Integer status() {
    	return this.value;
    }
    
}
