package dto;

public class TreePathDto extends TreeDto {

	public String path;
	
	public TreePathDto(Integer value, String label, String path) {
		super(value, label);
		this.path = path;
	}

}

