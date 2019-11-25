package services;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import beans.AttachmentBean;
import beans.MarcheAttachs;
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
import enums.MarcheTypeEnum;
import exceptions.OsIntegrityException;
import helpers.CONSTANTS;
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
	public Integer saveMarche(MarcheBean bean, MarcheAttachs attachs) throws IOException, ParseException {
		
		Map<String, Integer> idsMap = persistMarche(bean, attachs);
		
		Integer idProjet = idsMap.get("idProjet");
		Integer idMarche = idsMap.get("idMarche");
		
		setDefaultMarche(idProjet, idMarche, bean);
		
		return idMarche;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void setDefaultMarche(Integer idProjet, Integer idMarche, MarcheBean bean) {

		boolean isTravaux = bean.marcheType.value.equals(MarcheTypeEnum.travaux.value);

		if( isTravaux && ! marcheDao.getDefaultMarche(idProjet).equals(idMarche) ) {
			marcheDao.setDefaultMarche(idProjet, idMarche);
		}
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Map<String, Integer> persistMarche(MarcheBean bean, MarcheAttachs attachs) throws IOException, ParseException {


		boolean editMode = bean.idMarche != null ;

		Marches marche = editMode ? gMarchesDao.read(bean.idMarche, Marches.class) : new Marches();

		marche.setIntitule(bean.intitule);
		marche.setMontant(bean.montant);
		marche.setNumMarche(bean.numMarche);
		marche.setDelaiExecution(bean.delai);	
		marche.setMarchesType(new MarchesType(bean.marcheType.value));
		marche.setMarchesEtat(new MarchesEtat(bean.marcheEtat.value));
		marche.setDateApprobation(bean.dateApprobation);
		marche.setDateReceptionProvisoire(bean.dateReceptionProv);
		marche.setDateReceptionDefinitive(bean.dateReceptionDef);

		
		
		if( !editMode ) {
			marche.setProjet( new Projet(bean.idProjet) );
			marche.setDateSaisie(new Date());
			gMarchesDao.create(marche);
		}
		else {
//			marche.setDateLastModif(new Date());
		}
		


		marche.getMarchesSocietes().clear();
		entityManager.flush();
		
		
		//////////////////////////////////// SOCITIES
		
		if(bean.societes != null) {
			for(SimpleDto ste : bean.societes){
				marche.getMarchesSocietes().add(new MarchesSociete(marche, new Societe(ste.value)));
			}
		}

		//////////////////////////////////// ORDRES SERVICE
		
		
		boolean isOs = ! bean.os.isEmpty();
		boolean isTaux = ! bean.taux.isEmpty();

		List<MarchesOs> deletedOs = new ArrayList<MarchesOs>();
		long workDaysLastArretOrRecep = 0, workDaysCurrentTaux = 0;
		TauxBean newCurrentTaux = isTaux ? Collections.max(bean.taux, Comparator.comparing(tb -> tb.dateTaux)) : null;
		OsBean OsCurrentTaux = null;
		
		if( isOs ) {

			// on recupère d'abord les os qui ont été supprimés par UI on va les utiliser apres dans les attachements
			deletedOs = marche.getMarchesOss().stream().filter(oldOs -> bean.os.stream().noneMatch(os -> os.id != null && os.id.equals(oldOs.getId())))
							.collect(Collectors.toList());
			
			// on les supprime
//			marche.getMarchesOss().removeIf(os -> deletedOs.stream().anyMatch(del -> del.getId().equals(os.getId())));
			marche.getMarchesOss().removeIf(os -> bean.os.stream().noneMatch(del -> del.id != null && del.id.equals(os.getId())));
			
			// we have to sort les ordres de service par date
			bean.os.sort((o1, o2) -> o1.dateOs.compareTo(o2.dateOs));
			
			// we check if the first os is osStart
			OsBean osStartBean = bean.os.get(0);
			if( ! osStartBean.typeOs.value.equals(enums.OsType.COMMENCEMENT.value) ) {
				throw new OsIntegrityException();
			}
			

			// check integrity os
			int i=0;
			Integer previousOsType = enums.OsType.REPRISE.value; 			
			Date lastReprise = osStartBean.dateOs;
			
			for(OsBean os : bean.os) {
				
				if( i > 0 ) {
					
					if( os.typeOs.value.equals(previousOsType) ) { throw new OsIntegrityException(); } 
					previousOsType = os.typeOs.value;

					if(os.typeOs.value.equals(enums.OsType.ARRET.value)) {
						workDaysLastArretOrRecep += ChronoUnit.DAYS.between(Helpers.toLocalDate(lastReprise), Helpers.toLocalDate(os.dateOs));
					} else if(os.typeOs.value.equals(enums.OsType.REPRISE.value)) {
						lastReprise = os.dateOs;
					} else {
						throw new OsIntegrityException();
					}	
					
					boolean isOsBeforeCurrentTaux = isTaux && os.dateOs.before(newCurrentTaux.dateTaux);
					if(isOsBeforeCurrentTaux) {
						OsCurrentTaux = os;
						if(os.typeOs.value.equals(enums.OsType.ARRET.value)) {
							workDaysCurrentTaux = workDaysLastArretOrRecep;
						}
					}
					
//					if( os.typeOs.value.equals(enums.OsType.COMMENCEMENT.value) ) { throw new OsIntegrityException(); } 
				}

				MarchesOs mOs = new MarchesOs();
				
				// get existing os
				if(os.id != null) {
					mOs = marche.getMarchesOss().stream().filter( mos -> mos.getId().equals(os.id) ).findFirst().get();
				} 
				// add new os
				else {
					mOs.setMarches(marche);
					marche.getMarchesOss().add(mOs);
				}
				
				mOs.setCommentaire(os.commentaire);
				mOs.setDateOs(os.dateOs);
//				mOs.setDateOs(new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2019"));
//				mOs.setDateOs(new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2019"));
				mOs.setOsType(new OsType(os.typeOs.value));
				
				if( i == 0 ) {
					marche.setStartOs(mOs);
				}

				i++;
			}
			
			if( bean.dateReceptionProv != null ) {
				if(previousOsType.equals(enums.OsType.ARRET.value)) {					
					throw new OsIntegrityException();
				}
				
				workDaysLastArretOrRecep += ChronoUnit.DAYS.between(Helpers.toLocalDate(lastReprise), Helpers.toLocalDate(bean.dateReceptionProv));
			}
			
//			marche.setLastReprise(lastReprise);
			marche.setWorkDaysLastArretOrRecep(workDaysLastArretOrRecep);
			
			
			if(isTaux) {
				
				// si on a terminer avec une reprise alors on calcule days till date taux courant
				// pas de panic la date de newCurrentTaux > OsCurrentTaux
				// OsCurrentTaux null means => la date du taux est inferieure au 1er os (arret)
				if( OsCurrentTaux != null && OsCurrentTaux.typeOs.value.equals(enums.OsType.REPRISE.value) ) {
					workDaysCurrentTaux += ChronoUnit.DAYS.between(Helpers.toLocalDate(OsCurrentTaux.dateOs), Helpers.toLocalDate(newCurrentTaux.dateTaux));
				} 
				
				// 0 means que ya pas de OS arret ou reprise
				if(workDaysCurrentTaux == 0) {
					workDaysCurrentTaux = ChronoUnit.DAYS.between(Helpers.toLocalDate(marche.getStartOs().getDateOs()), Helpers.toLocalDate(newCurrentTaux.dateTaux) );
				}
			}
			
		} else {
			marche.setCurrentOs(null);
			marche.getMarchesOss().clear();
		}

		//////////////////////////////////// TAUX AVANCEMNT


		
		if( isTaux ) {

			// On supprime les taux qui ont etait supprimés par le user dans la page "Edit Marche"
			marche.getMarchesTaux().removeIf(mt -> bean.taux.stream().noneMatch( tb -> tb.id != null && tb.id.equals(mt.getId()) ));

			/// CHECK IF THE CURRENT TAUX HAS CHANGED
			MarchesTaux oldCurrentTaux = marche.getCurrentTaux();
			
			
//			if(  !editMode ||
//					oldCurrentTaux == null ||  
//					! oldCurrentTaux.getTaux().equals(newCurrentTaux.valueTaux) ||  
//					! Helpers.isEqual(newCurrentTaux.dateTaux, oldCurrentTaux.getDateTaux()) 
//			) {
//				System.out.println("TAUX COURANT HAS CHANGED !!");
//				tauxHasChanged = true;
//			}

			for(TauxBean tb : bean.taux) {
				MarchesTaux mT = new MarchesTaux();
				if( tb.id != null ) {		
					mT = marche.getMarchesTaux().stream().filter(mt -> mt.getId().equals(tb.id)).findFirst().get();
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
		
		List<MarchesDecomptes> deletedDec = new ArrayList<MarchesDecomptes>();
		
		if(isDec) {
			
			// on detect les DEC supprimés
			deletedDec = marche.getMarchesDecomptes().stream().filter( oldDec -> bean.decomptes.stream().noneMatch(dec -> dec.id.equals(oldDec.getId())) )
														.collect(Collectors.toList());
			
			marche.getMarchesDecomptes().removeIf(dec -> bean.decomptes.stream().noneMatch(del -> del.id != null && del.id.equals(dec.getId())));
			
			
			for(DecomptesBean dec : bean.decomptes) {
				MarchesDecomptes mDec = new MarchesDecomptes();
				
				if ( dec.id != null ) {
					mDec = marche.getMarchesDecomptes().stream().filter(mdec -> mdec.getId().equals(dec.id)).findFirst().get();
				} 
				else {
					mDec.setMarches(marche);
					marche.getMarchesDecomptes().add(mDec);
				}
				
				mDec.setDecompte(dec.montant);
				mDec.setDateDecompte(dec.dateDec);
				mDec.setCommentaire(dec.commentaire);

			}			
		} else {
			marche.setCurrentDecompte(null);
			marche.getMarchesDecomptes().clear();
		}
		
		entityManager.flush();
		
		marche.setCurrentTaux( isTaux ? Collections.max(marche.getMarchesTaux(), Comparator.comparing(mt -> mt.getDateTaux())) : null );
		marche.setCurrentOs( isOs ? Collections.max(marche.getMarchesOss(), Comparator.comparing(os -> os.getDateOs())) : null );
		marche.setCurrentDecompte( isDec ? Collections.max(marche.getMarchesDecomptes(), Comparator.comparing(dec -> dec.getDateDecompte())) : null );
		
		
		entityManager.flush();
		
		
		////////////////////////////////////////// WORKED DAYS && RETARD

		
		System.out.println("work Days since last Arrêt >>>>>>>>>>>>>>>> " + workDaysLastArretOrRecep);
		MarchesTaux currentTaux = marche.getCurrentTaux();
		
		if(isTaux) {

			int delaiInDays = marche.getDelaiExecution() * CONSTANTS.MONTH_DAYS ;
			int workPrecent = (int)  (workDaysCurrentTaux * 100 / delaiInDays) ;
			int currentTauxInDays = (int)  ( newCurrentTaux.valueTaux * delaiInDays / 100 );
			int retardEnJour = (int) workDaysCurrentTaux - currentTauxInDays;

			System.out.println("Delai Execution In Days >>>>>>>>>>>>>>>> " + delaiInDays);
			System.out.println("Le taux courant observé par l'agent sur terrain est de => (" + newCurrentTaux.valueTaux + " %) -> normalement il doit être à -> ("+currentTauxInDays+") jours");
			System.out.println("pour ce taux courant le nombres de jours effectués est de ("+workDaysCurrentTaux+") jours  -> normalement le taux doit être à (" + workPrecent + " %)");
			System.out.println( retardEnJour > 0 ? "retard encaissé de ("+retardEnJour+") jours" : "pas de retard ("+retardEnJour+")");
			
			
			currentTaux.setRetardEnJour( retardEnJour );
			currentTaux.setWorkedDays((int) workDaysCurrentTaux);
			
		} 
	
		
		//////////// SAVING ATTACHEMENTS

//		1. saving attachments	
		for(OsBean os : bean.os) {
			saveAttachments(
					os.resources, 
					os.index != null ? attachs.osAttachs[os.index] : null, 
					Helpers.getOsPathDate(marche.getId(), os.dateOs), 
					os.dateOs, marche.getId(), "OS-"
			);
		}
		
		for(DecomptesBean dec : bean.decomptes) {
			saveAttachments(dec.resources, 
					dec.index != null ? attachs.decAttachs[dec.index] : null, 
					Helpers.getDecPathDate(marche.getId(), dec.dateDec),  
					dec.dateDec, marche.getId(), "DEC-"
			);
		}
			
//		2. deleting attachments	
		for(MarchesOs del : deletedOs) {
			Helpers.deleteDir(Helpers.getOsPathDate(marche.getId(), del.getDateOs()));
		}
		
		for(MarchesDecomptes del : deletedDec) {
			Helpers.deleteDir(Helpers.getDecPathDate(marche.getId(), del.getDateDecompte()));
		}

//		3. deleting attachments	
		if(!isOs) Helpers.deleteDir(Helpers.getOsPath(marche.getId())); 		
		if(!isDec) Helpers.deleteDir(Helpers.getDecPath(marche.getId()));
			

		// returnig
		Map<String, Integer> idsMap = new HashMap<String, Integer>();
		idsMap.put("idMarche", marche.getId());
		idsMap.put("idProjet", marche.getProjet().getId());
		return idsMap;
	}
	
	

	public void saveAttachments(List<String> resources, List<MultipartFile> attachments, String savingPath, 
			Date namingDate, Integer idMarche, String prefix) throws IOException {
		
		boolean weAlreadyDelete = false;
		List<String> existingResources = Helpers.getDirFilesName(savingPath);
		
		
		int i = 1;

		if( ! resources.isEmpty() && existingResources.size() != resources.size() ) {

			System.out.println(">> resources iterating ...");
			for(String fileName : existingResources) {
				
				System.out.println(">> " + fileName);
				
				String currentPath = savingPath + "/" + fileName;
				
				boolean notExist = resources.stream().noneMatch(n -> n.equals(fileName));
				
				if ( notExist ) {
					System.out.println("deleting ................................ > : "+currentPath);
					Files.delete(Paths.get(currentPath));
					weAlreadyDelete = true;
					continue;
				}
				
				// RENAMNE RESOURCES
				if( weAlreadyDelete ) {
					String newName = Helpers.namingAttachFiles(savingPath, namingDate, idMarche, fileName, prefix, i, fileName.startsWith("IMG-"));
					System.out.println("renaming : "+ currentPath + " => : " + newName);
					Helpers.renameFile(currentPath, newName);
				}
				
				i++;

			}

		} else if( resources.isEmpty() ) {
			Helpers.deleteDir(savingPath);
		} else if( existingResources.size() == resources.size() ) {
			i+= existingResources.size();
		}

		// Saving attachements related to this OS
		if( attachments != null ) {

			for(MultipartFile mpFile : attachments) {
				
				Helpers.createFile(
						mpFile, 
						Helpers.namingAttachFiles(
								savingPath, namingDate, idMarche, mpFile.getOriginalFilename(), prefix, i++, mpFile.getContentType().startsWith("image/")
						)
				);
			}
		}
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
		
		Integer idMarche = marcheDao.getTravauxMarcheId(idProjet);
		
		if( idMarche == null )
			return null;
		
		return getMarcheForDetail(idMarche);

	}
	
	public  MarcheBean prepareMarcheDto(Integer idMarche) {
		
		
		Marches marche = marcheDao.getMarcheForEdit(idMarche);

		MarcheBean marcheDto = new MarcheBean(
				marche.getId(), marche.getProjet().getId(), marche.getIntitule(), marche.getDelaiExecution(),
				marche.getMontant(), marche.getNumMarche(), 
				marche.getDateApprobation(), marche.getDateReceptionProvisoire(), marche.getDateReceptionDefinitive()
		);

		marcheDto.marcheType = new SimpleDto(marche.getMarchesType().getId(), marche.getMarchesType().getNom());
		marcheDto.marcheEtat = new SimpleDto(marche.getMarchesEtat().getId(), marche.getMarchesEtat().getNom());

		
		if( marche.getDateReceptionProvisoire() != null ) {
			marcheDto.workDays = marche.getWorkDaysLastArretOrRecep();
		}
		else if( marche.getCurrentOs() != null ) {
			
			marcheDto.workDays = marche.getCurrentOs().getOsType().getId().equals(enums.OsType.ARRET.value) ?
					marche.getWorkDaysLastArretOrRecep()
					: 
					marche.getWorkDaysLastArretOrRecep() + ChronoUnit.DAYS.between(
						Helpers.toLocalDate(marche.getCurrentOs().getDateOs()), Helpers.toLocalDate(new Date())
					)
					;
		}
				
		
		marche.getMarchesTaux().forEach(taux -> {
			marcheDto.taux.add(new TauxBean(taux.getId(), taux.getTaux(), taux.getDateTaux(), taux.getCommentaire()));
		});
		
		
		marche.getMarchesSocietes().forEach(mSte -> {
			marcheDto.societes.add(new SimpleDto(mSte.getSociete().getId(), mSte.getSociete().getNom()));
		});
		
		marche.getMarchesDecomptes().forEach(dec -> {
			
					try {
						marcheDto.decomptes.add(new DecomptesBean(
								dec.getId(),
								dec.getDecompte(), 
								dec.getDateDecompte(), 
								dec.getCommentaire(), 
								Helpers.getDirFilesName(Helpers.getDecPathDate(marche.getId(), dec.getDateDecompte()))
						));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		});
		
		marche.getMarchesOss().forEach(os -> {
			
					try {
						
						OsBean dto = new OsBean(
								os.getId(),
								new SimpleDto(os.getOsType().getId(), 
								os.getOsType().getLabel()), 
								os.getDateOs(), 
								os.getCommentaire(),
								Helpers.getDirFilesName(Helpers.getOsPathDate(marche.getId(), os.getDateOs()))
						);
						
						if( os.getOsType().getId() == enums.OsType.COMMENCEMENT.value ) {
							marcheDto.osStart.add(dto);
						} else {							
							marcheDto.os.add(dto);
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		});
		
		return marcheDto;
	}


}
