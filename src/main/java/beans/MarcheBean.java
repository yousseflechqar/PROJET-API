package beans;

import java.util.List;

public class MarcheBean {
	
	
	public Integer idMarche;
	public Integer marcheType;
	public String intitule;
	public Integer delai;
	public Double montant;
	public List<Integer> societe;
	public String dateStart;
	public List<OsBean> os;
	public List<TauxBean> taux;
	public List<DecomptesBean> decomptes;
	public String dateReceptionProv;
	public String dateReceptionDef;
	
	
	
	static class OsBean {
		public Integer typeOs;
		public Integer dateOs;
		public Integer commentaire;
	}
	
	static class TauxBean {
		public Integer valueTaux;
		public Integer dateTaux;
		public Integer commentaire;
	}
	
	static class DecomptesBean {
		public Double montant;
		public Integer dateDec;
		public Integer commentaire;
	}


}
