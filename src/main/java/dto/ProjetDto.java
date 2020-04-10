package dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;





public class ProjetDto {
	
	public Integer id;
	public String intitule;
	public Integer chargeSuivID;
	public Integer taux;
	public String maitreOuvrage;
	
	public Map<Integer, String> localisations = new LinkedHashMap<Integer, String>();
	public Map<Integer, String> marches = new LinkedHashMap<Integer, String>();
	
	
	/**
	 * these fields helps us to custruct localisations & marches maps
	 */
	/// TEMP
	@JsonIgnore
	public Integer communeId;
	@JsonIgnore
	public String communeLabel;
	@JsonIgnore
	public Integer marcheId;
	@JsonIgnore
	public String marcheType;

	@QueryProjection
	public ProjetDto(Integer id, String intitule, Integer chargeSuivID, Integer taux, String maitreOuvrage, Integer communeId, String communeLabel,
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
		this.chargeSuivID = chargeSuivID;
	}
	
	
	

}
