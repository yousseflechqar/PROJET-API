package services;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
	public Integer saveMarche(MarcheBean bean, MarcheAttachs attachs) throws IOException {
		

		
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
//		marche.getMarchesOss().clear();
//		marche.getMarchesDecomptes().clear();
		
//		marche.setCurrentOs(null);
//		marche.setCurrentDecompte(null);
		
		

		entityManager.flush();
		
		
		//////////////////////////////////// SOCITIES
		
		if(bean.societes != null) {
			for(SimpleDto ste : bean.societes){
				marche.getMarchesSocietes().add(new MarchesSociete(marche, new Societe(ste.value)));
			}
		}

		
		
		
		//////////////////////////////////// ORDRES SERVICE
		
		boolean isOs = ( bean.os != null && ! bean.os.isEmpty() );

		List<MarchesOs> deletedOs = new ArrayList<MarchesOs>();
		
		if( isOs ) {
			
			
			// on detect les os supprimés
			deletedOs = marche.getMarchesOss().stream().filter( oldOs -> bean.os.stream().noneMatch(os -> os.id.equals(oldOs.getId())) )
														.collect(Collectors.toList());
			
			marche.getMarchesOss().removeIf(os -> bean.os.stream().noneMatch(del -> del.id != null && del.id.equals(os.getId())));
			
			
			// we have to sort les ordres de service par date
			bean.os.sort((o1, o2) -> o1.dateOs.compareTo(o2.dateOs));
			
			// check integrity os
			Integer previousOsType = enums.OsType.REPRISE.value;
			for(OsBean os : bean.os) {

				if( os.typeOs.value.equals(previousOsType) ) {
					throw new OsIntegrityException();
				} else {
					
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
					mOs.setOsType(new OsType(os.typeOs.value));

					previousOsType = os.typeOs.value;
				}
			}
		} else {
			marche.setCurrentOs(null);
			marche.getMarchesOss().clear();
		}

		//////////////////////////////////// TAUX AVANCEMNT

		
		boolean isTaux = bean.taux != null && ! bean.taux.isEmpty();
		
		if( isTaux ) {

			// On supprime les taux qui ont etait supprimés par le user dans la page "Edit Marche"
			marche.getMarchesTaux().removeIf(mt -> bean.taux.stream().noneMatch( tb -> tb.id != null && tb.id.equals(mt.getId()) ));

			/// CHECK IF THE CURRENT TAUX HAS CHANGED
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
					mT = marche.getMarchesTaux().stream().filter(mt -> mt.getId().equals(tb.id)).findFirst().get();
//					mT = marche.getMarchesTaux().stream().filter(mt -> mt.getId().equals(tb.id)).findAny().orElse(null);
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
		
		
		
//		for(OsBean os : bean.os) {

//			mrequest.getFile("attachOs[")
//			OutputStream OUT = Files.newOutputStream( 
//					Paths.get("D:/attachements/" + attach.file.getOriginalFilename()) 
//			) ;
//			IOUtils.copy( attach.file.getInputStream() , OUT ); 
//			OUT.close();
//		}
		
		
		//////////// SAVING ATTACHEMENTS
		
		if( isOs ) {
			
			
			
			for(OsBean os : bean.os) {
				saveAttachments(
						os.resources, 
						os.index != null ? attachs.osAttachs[os.index] : null, 
						Helpers.getOsPathDate(marche.getId(), os.dateOs), 
						os.dateOs, marche.getId(), "OS-"
				);
			}
			

			for(MarchesOs del : deletedOs) {
				Helpers.deleteDir(Helpers.getOsPathDate(marche.getId(), del.getDateOs()));
			}

			
		} else { Helpers.deleteDir(Helpers.getOsPath(marche.getId())); }
		
		
		if(isDec) {
			
			for(DecomptesBean dec : bean.decomptes) {
				saveAttachments(dec.resources, 
						dec.index != null ? attachs.decAttachs[dec.index] : null, 
						Helpers.getDecPathDate(marche.getId(), dec.dateDec),  
						dec.dateDec, marche.getId(), "DEC-"
				);
			}	
			

			for(MarchesDecomptes del : deletedDec) {
				Helpers.deleteDir(Helpers.getDecPathDate(marche.getId(), del.getDateDecompte()));
			}

			
		} else { Helpers.deleteDir(Helpers.getDecPath(marche.getId())); }

		return marche.getId();
		
		
		
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
				marche.getDateApprobation(), marche.getDateOsStart(), marche.getDateReceptionProvisoire(), marche.getDateReceptionDefinitive()
		);

		marcheDto.marcheType = new SimpleDto(marche.getMarchesType().getId(), marche.getMarchesType().getNom());
		marcheDto.marcheEtat = new SimpleDto(marche.getMarchesEtat().getId(), marche.getMarchesEtat().getNom());

		
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
						marcheDto.os.add(new OsBean(
								os.getId(),
								new SimpleDto(os.getOsType().getId(), 
								os.getOsType().getLabel()), 
								os.getDateOs(), 
								os.getCommentaire(),
								Helpers.getDirFilesName(Helpers.getOsPathDate(marche.getId(), os.getDateOs()))
						));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		});
		
		return marcheDto;
	}


}
