package enums;

public enum ProfilesEnum {

	
	root(1), charge_suivi(2), division_supervisor(3), supervisor_general(4), consultation(5); 

    public Integer value ;  
    
    private ProfilesEnum(Integer value) {  
        this.value = value ;  
    } 
    
}
