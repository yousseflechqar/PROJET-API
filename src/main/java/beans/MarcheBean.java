package beans;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class MarcheBean {
	
	
	public Integer idMarche;
	public Integer idProjet;
	public Integer marcheType;
	public Integer marcheEtat;
	public String intitule;
	public Integer delai;
	public Double montant;
	public JsonNode societes;
	public Date dateStart;
	public List<OsBean> os = new ArrayList<>();
	public List<TauxBean> taux = new ArrayList<>();
	public List<DecomptesBean> decomptes = new ArrayList<>();
	public Date dateReceptionProv;
	public Date dateReceptionDef;
	
	public MarcheBean() {}
	
	public MarcheBean(Integer idMarche, Integer marcheType, Integer marcheEtat, String intitule, Integer delai,
			Double montant, Date dateStart, Date dateReceptionProv, Date dateReceptionDef) {
		
		super();
		System.out.println(dateStart);
		this.idMarche = idMarche;
		this.marcheType = marcheType;
		this.marcheEtat = marcheEtat;
		this.intitule = intitule;
		this.delai = delai;
		this.montant = montant;
//		this.dateStart = new Date(dateStart.getTime());;
		this.dateStart = dateStart;
		this.dateReceptionProv = dateReceptionProv;
		this.dateReceptionDef = dateReceptionDef;
	}

	public static class OsBean {
		public Integer typeOs;
		public Date dateOs;
		public String commentaire;
		
		public OsBean() {}
		public OsBean(Integer typeOs, Date dateOs, String commentaire) {
			this.typeOs = typeOs;
			this.dateOs = dateOs;
			this.commentaire = commentaire;
		}
	}
	
	public static class TauxBean {
		public Integer valueTaux;
		public Date dateTaux;
		public String commentaire;
		
		public TauxBean() {}
		public TauxBean(Integer valueTaux, Date dateTaux, String commentaire) {
			this.valueTaux = valueTaux;
			this.dateTaux = dateTaux;
			this.commentaire = commentaire;
		}
	}
	
	public static class DecomptesBean {
		public Double montant;
		public Date dateDec;
		public String commentaire;
		
		public DecomptesBean() {}
		public DecomptesBean(Double montant, Date dateDec, String commentaire) {
			this.montant = montant;
			this.dateDec = dateDec;
			this.commentaire = commentaire;
		}
	}


}
