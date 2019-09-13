package enums;

public enum ContributionEnum {

	
	financiere(1), autres(2); 

    public Integer value ;  
    
    private ContributionEnum(Integer value) {  
        this.value = value ;  
    } 

}
