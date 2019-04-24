package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.sql.SQLExpressions;

import beans.ProjetSearchBean;
import dto.ProjetDto;
import dto.QProjetDto;
import entities.QAcheteur;
import entities.QCommune;
import entities.QLocalisation;
import entities.QProjet;
import entities.QProjetMaitreOuvrage;

@Repository
public class SearchProjetDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public List<ProjetDto> getListProjets(ProjetSearchBean bean){
		
		QProjet prj = new QProjet("prj");
		
		QProjetMaitreOuvrage pMo = new QProjetMaitreOuvrage("pMo");
		QAcheteur mo = new QAcheteur("mo");
		QLocalisation loc = new QLocalisation("loc");
		QCommune com = new QCommune("com"); 
		
		
		BooleanBuilder sWhere = new BooleanBuilder();
		
		if(bean.intitule != null) {
			sWhere.and(prj.intitule.contains(bean.intitule));
		}
		if(bean.secteur != null) {
			sWhere.and(prj.secteur.id.eq(bean.secteur));
		}		
		if(bean.maitreOuvrage != null) {
			sWhere.and(pMo.maitreOuvrage.id.eq(bean.maitreOuvrage));
		}
		
		////// LOCALISATION
		
		BooleanBuilder where_loc = new BooleanBuilder();
		

		
		
		if( bean.commune != null ){

			QLocalisation loc_2 = new QLocalisation("loc_2");
			QProjet prj_2 = new QProjet("prj_2");
			QCommune com_2 = new QCommune("com_2");

			where_loc.and(prj.id.in( 
					JPAExpressions.select(prj_2.id).from(prj_2)
						.join(prj_2.localisations, loc_2)
						.join(loc_2.commune, com_2)
					.where(com_2.id.eq(bean.commune))
					.fetch()
			));
			
		}
		
		return new JPAQuery<ProjetDto>(entityManager)

				
				.select(new QProjetDto(prj.id, prj.intitule, prj.taux, mo.nom, com.id, com.nom))
				.from(prj)
					.leftJoin(prj.projetMaitreOuvrage, pMo)
						.leftJoin(pMo.maitreOuvrage, mo)
					.leftJoin(prj.localisations, loc)
						.leftJoin(loc.commune, com)
				
				.where(sWhere.and(where_loc))
				.orderBy(prj.id.asc())
				.fetch();

	}
	
	
}
