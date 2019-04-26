package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;

import beans.ProjetSearchBean;
import dto.ProjetDto;
import dto.QProjetDto;
import entities.QAcheteur;
import entities.QCommune;
import entities.QLocalisation;
import entities.QProjet;
import entities.QProjetMaitreOuvrage;
import entities.QProjetPartenaire;

@Repository
public class SearchProjetDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	

	

	/////////////// QueryDsl
	
	public List<ProjetDto> getListProjets(ProjetSearchBean bean){
		
		QProjet prj = new QProjet("prj");
		
		QProjetMaitreOuvrage pMo = new QProjetMaitreOuvrage("pMo");
		QAcheteur mo = new QAcheteur("mo");
		QLocalisation loc = new QLocalisation("loc");
		QCommune com = new QCommune("com"); 
		
		
		BooleanBuilder sWhere = new BooleanBuilder();
		
		if(!bean.intitule.isEmpty()) {
			sWhere.and(prj.intitule.contains(bean.intitule));
		}
		if(bean.secteur != null) {
			sWhere.and(prj.secteur.id.eq(bean.secteur));
		}		
		
		
		////// MAITRE OUVRAGE ET PARTENAIRE

		BooleanBuilder where_ach = new BooleanBuilder();
		
		if(bean.acheteur != null) {
			if(bean.acheteurType.equals(1)) {				
				where_ach.and(pMo.maitreOuvrage.id.eq(bean.acheteur));
			}
			else if(bean.acheteurType.equals(2)) {
				QProjetPartenaire pPartner = new QProjetPartenaire("pPartner");
				where_ach.and(prj.id.in(JPAExpressions
						.select(pPartner.projet.id).from(pPartner)
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
		
		return new JPAQuery<ProjetDto>(entityManager)
				.select(new QProjetDto(prj.id, prj.intitule, prj.taux, mo.nom, com.id, com.nom))
				.from(prj)
					.leftJoin(prj.projetMaitreOuvrage, pMo)
						.leftJoin(pMo.maitreOuvrage, mo)
					.leftJoin(prj.localisations, loc)
						.leftJoin(loc.commune, com)
				
				.where(sWhere.and(where_loc).and(where_ach))
				.orderBy(prj.id.asc())
				.fetch()
				;

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
