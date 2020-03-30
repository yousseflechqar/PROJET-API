package dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;


@Repository
public class GenericDao<T, PK extends Serializable>  {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	public T persist(T t) {
	    this.entityManager.persist(t);
	    return t;
	}
	
	public T find(PK id, Class<T> entityClass) {
	    return this.entityManager.find(entityClass, id);
	}
	
	public T getReference(PK id, Class<T> entityClass) {
		return this.entityManager.getReference(entityClass, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> entityClass) {
		return entityManager.createQuery("Select t from " + entityClass.getSimpleName() + " t").getResultList();
	}
	
	
	public T update(T t) {
	    return this.entityManager.merge(t);
	}
	
	
	@Transactional
	public void delete(Class<T> entityClass, PK id) {
		entityManager.createQuery("DELETE FROM " + entityClass.getSimpleName() + " t "
				+ " WHERE t." + entityManager.getMetamodel().entity(entityClass).getDeclaredId(id.getClass()).getName() +" = :id ")
		.setParameter("id", id).executeUpdate();

	}
	
	@Transactional
	public void deleteAll(Class<T> entityClass) {
		entityManager.createQuery("delete from " + entityClass.getSimpleName() + " t ").executeUpdate();
	}
	
	
}
