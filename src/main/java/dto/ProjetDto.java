package dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.querydsl.core.annotations.QueryProjection;





public class ProjetDto {
	
	public Integer id;
	public String intitule;
	public Integer taux;
	public String maitreOuvrage;
	public Map<Integer, String> localisations = new LinkedHashMap<Integer, String>();
	public Map<Integer, String> marches = new LinkedHashMap<Integer, String>();
	
	/// TEMP
	public Integer communeId;
	public String communeLabel;
	public Integer marcheId;
	public String marcheType;

	@QueryProjection
	public ProjetDto(Integer id, String intitule, Integer taux, String maitreOuvrage, Integer communeId, String communeLabel,
			Integer marcheId, String marcheType
			) {
		this.id = id;
		this.intitule = intitule;
		this.taux = taux;
		this.maitreOuvrage = maitreOuvrage;
		this.communeId = communeId;
		this.communeLabel = communeLabel;
		this.marcheId = marcheId;
		this.marcheType = marcheType;
	}
	
	
	

}
