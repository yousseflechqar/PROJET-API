package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import dto.SimpleDto;

@Repository
public class MarcheDiversDao {
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@SuppressWarnings("unchecked")
	public List<SimpleDto> getOsTypes(){
		return entityManager.createQuery("SELECT new dto.SimpleDto(os.id, os.label) FROM OsType os").getResultList() ;
	}


	@SuppressWarnings("unchecked")
	public List<SimpleDto> getMarcheEtats() {
		return entityManager.createQuery("SELECT new dto.SimpleDto(me.id, me.nom) FROM MarchesEtat me").getResultList() ;
	}

}
