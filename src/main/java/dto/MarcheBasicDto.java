package dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import beans.MarcheBean.DecomptesBean;
import beans.MarcheBean.OsBean;
import beans.MarcheBean.TauxBean;

public class MarcheBasicDto <T> {
	
	
	public Integer idMarche;
	public Integer idProjet;

	public String intitule;
	public Integer delai;
	public Double montant;

	public Date dateStart;
	public List<OsBean> os = new ArrayList<>();
	public List<TauxBean> taux = new ArrayList<>();
	public List<DecomptesBean> decomptes = new ArrayList<>();
	public Date dateReceptionProv;
	public Date dateReceptionDef;
	
	
	public T marcheType;
	public T marcheEtat;
	public List<T> societes;
	

}



