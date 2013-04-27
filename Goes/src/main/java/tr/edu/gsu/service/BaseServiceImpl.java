package tr.edu.gsu.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import tr.edu.gsu.domain.BaseEntity;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
	private static final long serialVersionUID = 1L;
	protected static Log LOG = LogFactory.getLog(BaseService.class);

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected EntityManager em;

	protected Class<T> entityClass;

	public BaseServiceImpl(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		LOG.debug("Find all " + entityClass.getSimpleName());
		return em.createQuery("from " + entityClass.getSimpleName()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public T find(Long id) {
		try {
			if (id == null)
				return null;
			LOG.debug("find by id " + id + entityClass.getSimpleName());
			return (T) em.createQuery("select c from " + entityClass.getSimpleName() + " c where c.id =?").setParameter(1, id).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	@Transactional
	public void save(T entity) {
		if (entity == null)
			return;
		LOG.debug("Save entity: " + entityClass.getSimpleName());
		em.persist(entity);
	}

	@Transactional
	public T update(T entity) {
		if (entity == null)
			return null;
		LOG.debug("Update entity: " + entityClass.getSimpleName());
		T merged = em.merge(entity);
		em.flush();
		return merged;
	}

	@Transactional
	public void delete(T entity) {
		if (entity == null)
			return;
		LOG.debug("Delete entity: " + entityClass.getSimpleName());
		if (em.contains(entity)) {
			em.remove(entity);
		} else {
			T attached = find(entity.getId());
			if (attached != null) {
				em.remove(attached);
			}
		}
	}

	@Transactional
	public void refresh(T entity) {
		if (entity == null)
			return;
		LOG.debug("Refresh entity manager");
		em.refresh(entity);
	}

	@Transactional
	public void flush() {
		LOG.debug("Flush entity manager");
		em.flush();
	}
}
