package enums;

public enum OsType {

	ARRET(1), REPRISE(2), COMMENCEMENT(3); 

    public Integer value ;  
    
    private OsType(Integer value) {  
        this.value = value ;  
    } 
    
}
