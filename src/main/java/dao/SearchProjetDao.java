package dao;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;

import beans.ProjetSearchBean;
import dto.PageResult;
import dto.ProjetDto;
import dto.QProjetDto;
import entities.QAcheteur;
import entities.QCommune;
import entities.QLocalisation;
import entities.QMarches;
import entities.QMarchesOs;
import entities.QMarchesType;
import entities.QProjet;
import entities.QProjetMaitreOuvrage;
import entities.QProjetPartenaire;
import entities.QUser;
import enums.MarcheEtatEnum;
import enums.OsType;
import enums.ProjetStatus;
import helpers.CONSTANTS;


@Repository
public class SearchProjetDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	

	

	/////////////// QueryDsl
	
	public PageResult getListProjets(ProjetSearchBean bean){
		
		QProjet prj = new QProjet("prj");
		QProjetMaitreOuvrage pMo = new QProjetMaitreOuvrage("pMo");
		

		
		
		BooleanBuilder sWhere = new BooleanBuilder();
		
		if(! bean.intitule.isEmpty()) {
			sWhere.and(prj.intitule.contains(bean.intitule));
		}
		if(bean.secteur != null) {
			sWhere.and(prj.secteur.id.eq(bean.secteur));
		}		
		
		////// SRC FINANCEMENT
		
		BooleanBuilder where_srcFi = new BooleanBuilder();
		if( bean.srcFinancement != 0 ) {
			where_srcFi.and(prj.srcFinancement.id.eq(bean.srcFinancement));
		}
		
		////// MAITRE OUVRAGE ET PARTENAIRE

		BooleanBuilder where_ach = new BooleanBuilder();
		
		boolean pMoJoin = false;
		if(bean.acheteur != null) {

			if(bean.acheteurType.equals(1)) {				
				where_ach.and(pMo.maitreOuvrage.id.eq(bean.acheteur));
				pMoJoin = true;
			}
			else if(bean.acheteurType.equals(2)) {
				QProjetPartenaire pPartner = new QProjetPartenaire("pPartner");
				where_ach.and(prj.id.in(
						JPAExpressions.select(pPartner.projet.id).from(pPartner)
						.where(pPartner.partenaire.id.eq(bean.acheteur))
				));
			}	
		}
		
		////// LOCALISATION

		BooleanBuilder where_loc = new BooleanBuilder();
		
		if( bean.commune != null ){
			QLocalisation loc_2 = new QLocalisation("loc_2");
			where_loc.and(prj.id.in(JPAExpressions
					.select(loc_2.projet.id).from(loc_2)
					.where(loc_2.commune.id.eq(bean.commune))
					
			));
		}
		

		/////// PROJET EN SOUFFRANCE
		
		BooleanBuilder whereStatus = new BooleanBuilder();
		
		if( bean.projectStatus != 0 ){
			QMarches marche2 = new QMarches("marche2");

			if( bean.projectStatus.equals(ProjetStatus.EN_ARRET.status()) ) {
				QMarchesOs mOs2 = new QMarchesOs("mOs2");
				whereStatus.and(prj.id.in(JPAExpressions
						.select(marche2.projet.id).from(marche2).join(marche2.currentOs, mOs2)
						.where(mOs2.osType.id.eq(OsType.ARRET.value))
				));
			}
			
			else if(bean.projectStatus.equals(ProjetStatus.EN_COURS.status())) {
				whereStatus.and(prj.id.in(JPAExpressions
						.select(marche2.projet.id).from(marche2)
						.where(marche2.marchesEtat.id.in(
								Arrays.asList(MarcheEtatEnum.adjudication.value, MarcheEtatEnum.approbation.value, MarcheEtatEnum.realisation.value))
						)
				));
			}
			else if(bean.projectStatus.equals(ProjetStatus.ACHEVE.status())) {
				whereStatus.and(prj.id.in(JPAExpressions
						.select(marche2.projet.id).from(marche2)
						.where(marche2.marchesEtat.id.eq(MarcheEtatEnum.acheve.value))
						));
			}
			else if(bean.projectStatus.equals(ProjetStatus.RESILIE.status())) {
				whereStatus.and(prj.id.in(JPAExpressions
						.select(marche2.projet.id).from(marche2)
						.where(marche2.marchesEtat.id.eq(MarcheEtatEnum.resilie.value))
						));
			}
			
			
			else if(bean.projectStatus.equals(ProjetStatus.DELAI_DEPASSE.status())) {
				
				QMarchesOs mOs2 = new QMarchesOs("mOs2");

				BooleanBuilder whereDelai = new BooleanBuilder();
				
				whereDelai.and(
						mOs2.osType.id.when(enums.OsType.ARRET.value)
						.then(
								marche2.workDaysLastArret.gt(marche2.delaiExecution.multiply(CONSTANTS.MONTH_DAYS))
						)
						
						.otherwise(
								marche2.workDaysLastArret
								.add(Expressions.numberTemplate(Integer.class, "datediff(NOW(), {0})", marche2.lastReprise))
								.gt(marche2.delaiExecution.multiply(CONSTANTS.MONTH_DAYS))
						)
//						.eq(true)
				);
				
				

				whereStatus.and(prj.id.in(JPAExpressions
						.select(marche2.projet.id).from(marche2).leftJoin(marche2.currentOs, mOs2)
						.where( 
//								whereDelai
								
								(
									mOs2.osType.id.eq(enums.OsType.ARRET.value).and(
											marche2.workDaysLastArret
											.gt(marche2.delaiExecution.multiply(CONSTANTS.MONTH_DAYS)))
								)
								.or
								(
									mOs2.osType.id.ne(enums.OsType.ARRET.value).and(
											marche2.workDaysLastArret
											.add(Expressions.numberTemplate(Integer.class, "datediff(NOW(), {0})", marche2.lastReprise))
											.gt(marche2.delaiExecution.multiply(CONSTANTS.MONTH_DAYS)))
								)
							    
////							    (
//								mOs2.osType.id.when(enums.OsType.ARRET.value)
//								.then(
//										marche2.workDaysLastArret.gt(marche2.delaiExecution.multiply(CONSTANTS.MONTH_DAYS))
//								)
//								
//								.otherwise(
//										marche2.workDaysLastArret
//										.add(Expressions.numberTemplate(Integer.class, "datediff(NOW(), {0})", marche2.lastReprise))
//										.gt(marche2.delaiExecution.multiply(CONSTANTS.MONTH_DAYS))
//								)
////								)
////							    .eq(true)

						)));
			}
		}

		///////////////////////////////// building the query
		
		PageResult page = new PageResult();
		Long a,b;
		
		JPAQuery<Integer> query = new JPAQuery<Integer>(entityManager).select(prj.id).from(prj);

		if(pMoJoin) {
			query.leftJoin(prj.projetMaitreOuvrage, pMo);
		}
		
		query.where(sWhere.and(where_loc).and(where_ach).and(where_srcFi).and(whereStatus));
		
		// count
		a = System.currentTimeMillis();
		if(bean.count) {			
			page.totalElements = query.clone().fetchCount();	
		}
		b = System.currentTimeMillis();
		System.out.println(">> Count -> " + (b - a));
		
		
		// getting ids first -> if we didn't do so and we specify size to 10 we will not retrieve the 10 first rows because there will be duplicates
		
		OrderSpecifier<Date> orderBy = prj.dateSaisie.desc();

		a = System.currentTimeMillis();
		List<Integer> ids = query.orderBy(orderBy)
									.offset((bean.page-1)*bean.size)
										.limit(bean.size)
											.fetch();
		b = System.currentTimeMillis();
		System.out.println(">> ids -> " + (b - a));

		
		// fetch all fields
		a = System.currentTimeMillis();
		
		QMarches marche = new QMarches("marche");
		QMarchesType mType = new QMarchesType("mType");
		QUser chargeSuiv = new QUser("chargeSuiv");
		QAcheteur mo = new QAcheteur("mo");
		QLocalisation loc = new QLocalisation("loc");
		QCommune com = new QCommune("com"); 
		QMarchesOs mOs = new QMarchesOs("mOs");

	
		page.content = new JPAQuery<ProjetDto>(entityManager)
				
				.select(
						new QProjetDto(prj.id, prj.intitule, prj.taux, mo.nom, com.id, com.nom, marche.id, mType.nom, 
						
						mOs.osType.id.when(enums.OsType.ARRET.value).then(marche.workDaysLastArret)
						.otherwise(marche.workDaysLastArret.add(Expressions.numberTemplate(Integer.class, "datediff(NOW(), {0})", marche.lastReprise)))
						
						)
				)

				.from(prj)
					.leftJoin(prj.chargeSuivi, chargeSuiv)
					.leftJoin(prj.marches, marche)
//					.leftJoin(prj.marches, marche)
						.leftJoin(marche.marchesType, mType)
						.leftJoin(marche.currentOs, mOs)
					.leftJoin(prj.projetMaitreOuvrage, pMo)
						.leftJoin(pMo.maitreOuvrage, mo)
					.leftJoin(prj.localisations, loc)
						.leftJoin(loc.commune, com)
				
				.where(prj.id.in(ids))
				.orderBy(orderBy)
				.fetch()
				;
		b = System.currentTimeMillis();
		System.out.println(">> Fetch -> " + (b - a));
		
		return page;

	}
	
	
	//////////// Criteria
	
//	@SuppressWarnings({"rawtypes", "unchecked"})
//	public List<ProjetDto> getListProjetsCriteria(ProjetSearchBean bean){
//		
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery cr = cb.createQuery(ProjetDto.class);
//		Root projet = cr.from(Projet.class);
//		
//
//		Join<Projet, ProjetMaitreOuvrage> pmo = projet.join("projetMaitreOuvrage", JoinType.INNER);
//		Join<ProjetMaitreOuvrage, Acheteur> mo = pmo.join("maitreOuvrage", JoinType.INNER);
//		Join<Projet, Localisation> loc = projet.join("localisations", JoinType.INNER);
//		Join<Localisation, Commune> com = loc.join("commune", JoinType.INNER);
//		
//		cr.select(
//				cb.construct(ProjetDto.class,
//					projet.get("id"),
//					projet.get("intitule"),
//					projet.get("taux"),
//					mo.get("nom"),
//					com.get("id"),
//					com.get("nom")
//			    )
//		);
//
//		Predicate sPredic = null;
//		
//		if(!bean.intitule.isEmpty()) {
//			sPredic = cb.like(projet.get("intitule"), "%"+bean.intitule+"%");
//		}
//
//		if(bean.secteur != null) {
//			sPredic = cb.equal(projet.get("secteur"), bean.secteur);
//		}
//		
//		if(bean.maitreOuvrage != null) {
//			sPredic = cb.equal(pmo.get("maitreOuvrage"), bean.maitreOuvrage);
//		}
//		
//		cr.where(sPredic)
//		;
//		 
//
//		
//		List<ProjetDto> results = entityManager.createQuery(cr).getResultList();
//
//
//		return results;
//	}
	
	
}
