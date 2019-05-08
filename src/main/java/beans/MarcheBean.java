package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dto.SimpleDto;

public class MarcheBean {
	
	
	public Integer idMarche;
	public Integer idProjet;

	public String intitule;
	public Integer delai;
	public Double montant;

	public Date dateStart;

	public List<TauxBean> taux = new ArrayList<>();
	public List<DecomptesBean> decomptes = new ArrayList<>();
	public Date dateReceptionProv;
	public Date dateReceptionDef;
	
	public List<OsBean> os = new ArrayList<>();
	public SimpleDto marcheType;
	public SimpleDto marcheEtat;
	public List<SimpleDto> societes = new ArrayList<>();
	
	public MarcheBean() {}
	
	public MarcheBean(Integer idMarche, Integer idProjet, String intitule, Integer delai,
			Double montant, Date dateStart, Date dateReceptionProv, Date dateReceptionDef) {
		
		super();
		this.idMarche = idMarche;
		this.idProjet = idProjet;
		this.intitule = intitule;
		this.delai = delai;
		this.montant = montant;
		this.dateStart = dateStart;
		this.dateReceptionProv = dateReceptionProv;
		this.dateReceptionDef = dateReceptionDef;
	}

	public static class OsBean {
		public SimpleDto typeOs;
		public Date dateOs;
		public String commentaire;
		
		public OsBean() {}
		public OsBean(SimpleDto typeOs, Date dateOs, String commentaire) {
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
