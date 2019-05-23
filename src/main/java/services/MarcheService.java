package services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import exceptions.OsIntegrityException;
import helpers.Helpers;

@Service
public class MarcheService {
	
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private MarcheDao marcheDao;
//	@Autowired
//	private GenericDao<Projet, Integer> gProjetDao;
	@Autowired
	private GenericDao<Marches, Integer> gMarchesDao;

	
	@Transactional(rollbackOn = Exception.class)
	public Integer saveMarche(MarcheBean bean) {
		
		
		boolean dirtyCheckTaux = false;

		Marches marche = new Marches();

		if( bean.idMarche != null ) {
			marche = gMarchesDao.read(bean.idMarche, Marches.class);
		} else {
			marche.setProjet( new Projet(bean.idProjet) );
		}

		marche.setIntitule(bean.intitule);
		marche.setMontant(bean.montant);
		
		if( bean.delai != null && ! bean.delai.equals(marche.getDelaiExecution()) ) {
			System.out.println("Delai Execution CHANGED !!");
			dirtyCheckTaux = true;
		}
		
		marche.setDelaiExecution(bean.delai);	
		marche.setNumMarche(bean.numMarche);
		marche.setMarchesType(new MarchesType(bean.marcheType.value));
		marche.setMarchesEtat(new MarchesEtat(bean.marcheEtat.value));
		
		marche.setDateApprobation(bean.dateApprobation);
		
		if( bean.dateStart != null && ! Helpers.isEqual(bean.dateStart, marche.getDateOsStart()) ) {
			System.out.println("Date Start CHANGED !!");
			dirtyCheckTaux = true;
		}
		
		marche.setDateOsStart(bean.dateStart);
		marche.setDateReceptionProvisoire(bean.dateReceptionProv);
		marche.setDateReceptionDefinitive(bean.dateReceptionDef);

		marche.setDateSaisie(new Date());
		
		if( bean.idMarche == null ) {
			gMarchesDao.create(marche);
		}
		

		
		marche.getMarchesSocietes().clear();
		marche.getMarchesOss().clear();
		marche.getMarchesDecomptes().clear();
		
		marche.setCurrentOs(null);
		marche.setCurrentDecompte(null);
		
		

		entityManager.flush();
		
		
		//////////////////////////////////// SOCITIES
		
		if(bean.societes != null) {
			for(SimpleDto ste : bean.societes){
				marche.getMarchesSocietes().add(new MarchesSociete(marche, new Societe(ste.value)));
			}
		}

		
		
		
		//////////////////////////////////// ORDRES SERVICE
		
		boolean isOs = ( bean.os != null && ! bean.os.isEmpty() );
		if( isOs ) {
			// we have to sort les ordres de service par date
			bean.os.sort((o1, o2) -> o1.dateOs.compareTo(o2.dateOs));
			
			// check integrity os
			Integer previousOsType = enums.OsType.REPRISE.value;
			for(OsBean os : bean.os) {
				if( os.typeOs.value.equals(previousOsType) ) {
					throw new OsIntegrityException();
				} else {
					marche.getMarchesOss().add(new MarchesOs(marche, new OsType(os.typeOs.value), os.dateOs, os.commentaire));
					previousOsType = os.typeOs.value;
				}
			}
		}


		//////////////////////////////////// TAUX AVANCEMNT
		
		boolean isTaux = bean.taux != null && !bean.taux.isEmpty();
		if( isTaux ) {
			
			// delete 
			marche.getMarchesTaux().removeIf(mt -> bean.taux.stream().noneMatch(tb -> tb.id != null && tb.id.equals(mt.getId())));

			/// CHECK IF TAUX HAS CHANGED
			MarchesTaux oldCurrentTaux = marche.getCurrentTaux();
			TauxBean newCurrentTaux = Collections.max(bean.taux, Comparator.comparing(tb -> tb.dateTaux));
			
			if( oldCurrentTaux == null ||  
					! oldCurrentTaux.getTaux().equals(newCurrentTaux.valueTaux) ||  
					! Helpers.isEqual(newCurrentTaux.dateTaux, oldCurrentTaux.getDateTaux()) ) {
				System.out.println("TAUX CHANGED !!");
				dirtyCheckTaux = true;
			}

			for(TauxBean tb : bean.taux) {
				MarchesTaux mT = new MarchesTaux();
				if( tb.id != null ) {		
					mT = marche.getMarchesTaux().stream().filter(mt -> mt.getId().equals(tb.id)).findAny().orElse(null);
				} 
				mT.setTaux(tb.valueTaux);
				mT.setDateTaux(tb.dateTaux);
				mT.setCommentaire(tb.commentaire);	
				// add new ones
				if( tb.id == null ) {	
					mT.setMarches(marche);
					marche.getMarchesTaux().add(mT);
				} 
			}
			
		} else {
			marche.setCurrentTaux(null);
			marche.getMarchesTaux().clear();
		}


		//////////////////////////////////// DECOMPTES
		
		boolean isDec = bean.decomptes != null && !bean.decomptes.isEmpty();
		if(isDec) {
			for(DecomptesBean db : bean.decomptes) {
				marche.getMarchesDecomptes().add(new MarchesDecomptes(marche, db.montant, db.dateDec, db.commentaire));
			}			
		}
		
		entityManager.flush();
		
		marche.setCurrentTaux( isTaux ? Collections.max(marche.getMarchesTaux(), Comparator.comparing(mt -> mt.getDateTaux())) : null );
		marche.setCurrentOs( isOs ? Collections.max(marche.getMarchesOss(), Comparator.comparing(os -> os.getDateOs())) : null );
		marche.setCurrentDecompte( isDec ? Collections.max(marche.getMarchesDecomptes(), Comparator.comparing(dec -> dec.getDateDecompte())) : null );
		
		
		entityManager.flush();
		
		
		////////////////////////////////////////// WORKED DAYS && RETARD

		if( isTaux && dirtyCheckTaux ) {

			MarchesTaux currentTaux = marche.getCurrentTaux();
			
			// filtrer les os qui ont une date superieur à la date du current taux
			List<OsBean> filtredOs = isOs ? bean.os.stream().filter(os -> os.dateOs.before(currentTaux.getDateTaux())).collect(Collectors.toList()) : null;

			long workDays = 0;
			if( filtredOs != null && !filtredOs.isEmpty() ) {

				Date lastReprise = marche.getDateOsStart();
				OsBean currentOs = null;
				
				for(OsBean os : filtredOs) {
					currentOs = os;
					if(os.typeOs.value.equals(enums.OsType.ARRET.value)) {
						workDays += ChronoUnit.DAYS.between( Helpers.toLocalDate(lastReprise), Helpers.toLocalDate(os.dateOs) );
					} else if(os.typeOs.value.equals(enums.OsType.REPRISE.value)) {
						lastReprise = os.dateOs;
					} else {
						throw new OsIntegrityException();
					}	
				}
				
				if( currentOs.typeOs.value.equals(enums.OsType.REPRISE.value) ) {
					workDays += ChronoUnit.DAYS.between( Helpers.toLocalDate(currentOs.dateOs), Helpers.toLocalDate(currentTaux.getDateTaux()) );
				} 

			} else {
				workDays = ChronoUnit.DAYS.between( Helpers.toLocalDate(marche.getDateOsStart()), Helpers.toLocalDate(currentTaux.getDateTaux()) );
			}

			int delaiInDays = marche.getDelaiExecution() * 31 ;
			int workPrecent = (int)  (workDays * 100 / delaiInDays) ;
			int currentTauxInDays = (int)  ( currentTaux.getTaux() * delaiInDays / 100 );
			int retardEnJour = (int) workDays - currentTauxInDays;

			System.out.println("Delai Execution In Days >>>>>>>>>>>>>>>> " + delaiInDays);
			System.out.println("Real >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + currentTaux.getTaux() + " % -> ("+currentTauxInDays+") jours");
			System.out.println("Théorique >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + workPrecent + " % -> ("+workDays+") jours");
			System.out.println("Retard >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ("+retardEnJour+") jours");
			
			currentTaux.setRetardEnJour( retardEnJour );
			currentTaux.setWorkedDays((int) workDays);
			
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
				marche.getMontant(), marche.getNumMarche(), 
				marche.getDateApprobation(), marche.getDateOsStart(), marche.getDateReceptionProvisoire(), marche.getDateReceptionDefinitive()
		);

		marcheDto.marcheType = new SimpleDto(marche.getMarchesType().getId(), marche.getMarchesType().getNom());
		marcheDto.marcheEtat = new SimpleDto(marche.getMarchesEtat().getId(), marche.getMarchesEtat().getNom());

		
		marche.getMarchesTaux().forEach(taux -> {
			marcheDto.taux.add(new TauxBean(taux.getId(), taux.getTaux(), taux.getDateTaux(), taux.getCommentaire()));
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
