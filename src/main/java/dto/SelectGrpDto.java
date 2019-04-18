package dto;

import java.util.ArrayList;
import java.util.List;

public class SelectGrpDto {

	public String label;
	public List<SimpleDto> options = new ArrayList<SimpleDto>();
	
	public SelectGrpDto() {}
	public SelectGrpDto(String label) {
		super();
		this.label = label;
	}

	
	
	
}
