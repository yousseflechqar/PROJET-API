package dto;

import java.util.ArrayList;
import java.util.List;

public class TreeDto {
	
	public Integer value;
	public String label;
	public String path;
	public List<TreeDto> children = new ArrayList<TreeDto>();
	
	public TreeDto(Integer value, String label, String path) {
		this.value = value;
		this.label = label;
		this.path = path;
//		if(createChildren) {
//			this.children = new ArrayList<TreeDto>();
//		}
	}
	
	

}
