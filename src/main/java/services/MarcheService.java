package services;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beans.MarcheBean;
import beans.MarcheBean.DecomptesBean;
import beans.MarcheBean.OsBean;
import beans.MarcheBean.TauxBean;
import dao.GenericDao;
import dao.MarcheDao;
import dto.SimpleDto;
import entities.Marches;
import entities.MarchesDecomptes;
import entities.MarchesEtat;
import entities.MarchesOs;
import entities.MarchesSociete;
import entities.MarchesTaux;
import entities.MarchesType;
import entities.OsType;
import entities.Projet;
import entities.Societe;

@Service
public class MarcheService {
	
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private MarcheDao marcheDao;
	@Autowired
	private GenericDao<Projet, Integer> gProjetDao;
	@Autowired
	private GenericDao<Marches, Integer> gMarchesDao;

	
	@Transactional(rollbackOn = Exception.class)
	public Integer saveMarche(MarcheBean bean) throws IOException {
		
		
		Projet projet = new Projet();
		Marches marche = new Marches();

		if( bean.idMarche != null ) {
			marche = gMarchesDao.read(bean.idMarche, Marches.class);
			projet = marche.getProjet();
		} else {
			projet = gProjetDao.read(bean.idProjet, Projet.class);
		}
		
		marche.setProjet( projet ); // a minimiser
		marche.setIntitule(bean.intitule);
		marche.setMontant(bean.montant);
		marche.setDelaiExecution(bean.delai);		
		marche.setMarchesType(new MarchesType(bean.marcheType.value));
		marche.setMarchesEtat(new MarchesEtat(bean.marcheEtat.value));
		
		marche.setDateOsStart(bean.dateStart);
		marche.setDateReceptionProvisoire(bean.dateReceptionProv);
		marche.setDateReceptionDefinitive(bean.dateReceptionDef);

		marche.setDateSaisie(new Date());
		
		if( bean.idMarche == null ) {
			gMarchesDao.create(marche);
		}
		
		marche.getMarchesSocietes().clear();
		marche.getMarchesOss().clear();
		marche.getMarchesTaux().clear();
		marche.getMarchesDecomptes().clear();
		
		entityManager.flush();
		
		if(bean.societes != null) {
			for(SimpleDto ste : bean.societes){
				marche.getMarchesSocietes().add(new MarchesSociete(marche, new Societe(ste.value)));
			}
		}

		if(bean.taux != null) {
			for(TauxBean tb : bean.taux){
				marche.getMarchesTaux().add(new MarchesTaux(marche, tb.valueTaux, tb.dateTaux, tb.commentaire));
			}
		}

		if(bean.os != null)
		for(OsBean os : bean.os){
			marche.getMarchesOss().add(new MarchesOs(marche, new OsType(os.typeOs.value), os.dateOs, os.commentaire));
		}

		if(bean.decomptes != null)
		for(DecomptesBean db : bean.decomptes){
			marche.getMarchesDecomptes().add(new MarchesDecomptes(marche, db.montant, db.dateDec, db.commentaire));
		}
		
		
		
		entityManager.flush();
		
		
		if(bean.taux != null) {
			marche.setCurrentTaux(Collections.max(marche.getMarchesTaux(), Comparator.comparing(mt -> mt.getDateTaux())));
		}
		
		return marche.getId();
		
		
		
	}

	public MarcheBean getMarcheForEdit(Integer idMarche) {

		MarcheBean marcheDto = prepareMarcheDto(idMarche);

		return marcheDto;
	}
	
	public MarcheBean getMarcheForDetail(Integer idMarche) {

		MarcheBean marcheDto = prepareMarcheDto(idMarche);

		return marcheDto;
	}
	

	public MarcheBean getDefaultMarcheForDetail(Integer idProjet) {

//		List<Integer> marchesIds = marcheDao.getMarchesIdsByProjet(idProjet);
		
		Integer travauxMarcheId = marcheDao.getTravauxMarcheId(idProjet);
		
		if( travauxMarcheId == null )
			return null;
		
		return getMarcheForDetail(travauxMarcheId);

	}
	
	public  MarcheBean prepareMarcheDto(Integer idMarche) {
		
		
		Marches marche = marcheDao.getMarcheForEdit(idMarche);

		MarcheBean marcheDto = new MarcheBean(
				marche.getId(), marche.getProjet().getId(), marche.getIntitule(), marche.getDelaiExecution(),
				marche.getMontant(), marche.getDateOsStart(), marche.getDateReceptionProvisoire(), marche.getDateReceptionDefinitive()
				);

		marcheDto.marcheType = new SimpleDto(marche.getMarchesType().getId(), marche.getMarchesType().getNom());
		marcheDto.marcheEtat = new SimpleDto(marche.getMarchesEtat().getId(), marche.getMarchesEtat().getNom());

		
		marche.getMarchesTaux().forEach(taux -> {
			marcheDto.taux.add(new TauxBean(taux.getTaux(), taux.getDateTaux(), taux.getCommentaire()));
		});
		
		marche.getMarchesDecomptes().forEach(dec -> {
			marcheDto.decomptes.add(new DecomptesBean(dec.getDecompte(), dec.getDateDecompte(), dec.getCommentaire()));
		});
		
		marche.getMarchesSocietes().forEach(mSte -> {
			marcheDto.societes.add(new SimpleDto(mSte.getSociete().getId(), mSte.getSociete().getNom()));
		});
		
		marche.getMarchesOss().forEach(os -> {
			marcheDto.os.add(new OsBean(new SimpleDto(os.getOsType().getId(), os.getOsType().getLabel()), os.getDateOs(), os.getCommentaire()));
		});
		
		return marcheDto;
	}


}
