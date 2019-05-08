package dto;

import java.util.ArrayList;
import java.util.List;

public class TreeDto {
	
	public Integer value;
	public String label;
	public List<TreeDto> children = new ArrayList<>();
	
	public TreeDto() {}
	
	public TreeDto(Integer value, String label) {
		super();
		this.value = value;
		this.label = label;
	}
	
	

}
