package dto;

import java.util.List;

import beans.MarcheBean;

public class DetailDto {
	
	public ProjetBasicDto<TreeDto, SimpleDto> projet;
	public MarcheBean defaultMarche;
	public List<SimpleDto> marchesTypes;
	
	
	public DetailDto(ProjetBasicDto<TreeDto, SimpleDto> projet, MarcheBean defaultMarche) {
		this.projet = projet;
		this.defaultMarche = defaultMarche;
	}
	
	public DetailDto(ProjetBasicDto<TreeDto, SimpleDto> projet, MarcheBean defaultMarche,
			List<SimpleDto> marchesTypes) {
		this.projet = projet;
		this.defaultMarche = defaultMarche;
		this.marchesTypes = marchesTypes;
	}
	
	

}
