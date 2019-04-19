package dto;

public class SelectGrpsItemDto  extends SimpleDto {
	
	public SelectGrpDto selectGrp;
	
	public SelectGrpsItemDto(Integer value, String label, SelectGrpDto selectGrp) {
		super(value, label);
		this.selectGrp=selectGrp;
	}
	public SelectGrpsItemDto(Integer value, String label) {
		super(value, label);
	}

}
