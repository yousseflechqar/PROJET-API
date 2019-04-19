package dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SelectGrpDto<T> {

	public String label;
	public Collection<T> options = new ArrayList<T>();
//	public Collection<SelectGrpDto> selectGrps = new ArrayList<SelectGrpDto>();
	
	public SelectGrpDto() {}
	public SelectGrpDto(String label) {
		super();
		this.label = label;
	}
	public SelectGrpDto(String label, Collection<T> options) {
		super();
		this.label = label;
		this.options = options;
	}

	
	public void addOption(T t){
		options.add(t);
    }

	
	
	
}
