package enums;

public enum MarcheTypeEnum {

	travaux(1), etudes(2), consultation_architecturale(3), autres(4); 

    public Integer value ;  
    
    private MarcheTypeEnum(Integer value) {  
        this.value = value ;  
    } 

    
}
