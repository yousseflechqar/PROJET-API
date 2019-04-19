package dto;

public class TreeProgrammeDto extends TreeDto {

	public Integer phase;
	
	public TreeProgrammeDto(Integer value, String label, Integer phase) {
		super(value, label);
		this.phase = phase;
	}

}
