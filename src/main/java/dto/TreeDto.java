package dto;

import java.util.ArrayList;
import java.util.List;

public class TreeDto {
	
	public Integer value;
	public String label;
	public List<TreeDto> children = new ArrayList<TreeDto>();
	
	public TreeDto(Integer value, String label) {
		this.value = value;
		this.label = label;
//		if(createChildren) {
//			this.children = new ArrayList<TreeDto>();
//		}
	}
	
	

}
