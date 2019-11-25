package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import dto.SimpleDto;

public class MarcheBean {
	
	
	public Integer idMarche;
	public Integer idProjet;

	public String intitule;
	public String numMarche;
	public Integer delai;
	public Long workDays = 0L ;
	public Double montant;

//	public OsBean osStart;

	public List<TauxBean> taux = new ArrayList<>();
	public List<DecomptesBean> decomptes = new ArrayList<>();
	public Date dateApprobation;
	public Date dateReceptionProv;
	public Date dateReceptionDef;
	
	public List<OsBean> os = new ArrayList<>();
	public List<OsBean> osStart = new ArrayList<>();
	
	public SimpleDto marcheType;
	public SimpleDto marcheEtat;
	public List<SimpleDto> societes = new ArrayList<>();
	

//	public boolean isDefault = false;
	
	public MarcheBean() {}
	
	public MarcheBean(Integer idMarche, Integer idProjet, String intitule, Integer delai,
			Double montant, String numMarche,
			Date dateApprobation, Date dateReceptionProv, Date dateReceptionDef) {
		
		super();
		this.idMarche = idMarche;
		this.idProjet = idProjet;
		this.intitule = intitule;
		this.delai = delai;
		this.numMarche = numMarche;
		this.montant = montant;
		this.dateReceptionProv = dateReceptionProv;
		this.dateReceptionDef = dateReceptionDef;
		this.dateApprobation = dateApprobation;
	}

	public static class OsBean {
		
		public Integer id;
		public SimpleDto typeOs;
		public Date dateOs;
		public String commentaire;
		public Integer index;
		public List<String> resources = new ArrayList<String>();
		
		public OsBean() {}
		
		public OsBean(Integer id, SimpleDto typeOs, Date dateOs, String commentaire, List<String> resources) {
			this.id = id;
			this.typeOs = typeOs;
			this.dateOs = dateOs;
			this.commentaire = commentaire;
			this.resources = resources;
		}

	}
	
	public static class TauxBean {
		
		public Integer id;
		public Integer valueTaux;
		public Date dateTaux;
		public String commentaire;
		
		public TauxBean() {}
		
		public TauxBean(Integer id, Integer valueTaux, Date dateTaux, String commentaire) {
			this.id = id;
			this.valueTaux = valueTaux;
			this.dateTaux = dateTaux;
			this.commentaire = commentaire;
		}


		
	}
	
	public static class DecomptesBean {
		
		public Integer id;
		public Double montant;
		public Date dateDec;
		public String commentaire;
		public Integer index;
		public List<String> resources = new ArrayList<String>();
		
		public DecomptesBean() {}
		
		public DecomptesBean(Integer id, Double montant, Date dateDec, String commentaire, List<String> resources) {
			this.id = id;
			this.montant = montant;
			this.dateDec = dateDec;
			this.commentaire = commentaire;
			this.resources = resources;
		}

	}


}
