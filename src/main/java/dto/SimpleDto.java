package dto;

public class SimpleDto {

	public Integer value;
	public String label;
	
	public SimpleDto() {}
	public SimpleDto(Integer value, String label) {
		this.value = value;
		this.label = label;
	}
	public SimpleDto(Integer value) {
		this.value = value;
	}
	
}
