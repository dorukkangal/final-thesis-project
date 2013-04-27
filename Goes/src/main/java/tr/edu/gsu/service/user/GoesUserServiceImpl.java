package tr.edu.gsu.service.user;

import javax.persistence.NoResultException;

import org.springframework.dao.EmptyResultDataAccessException;

import tr.edu.gsu.domain.user.GoesUser;
import tr.edu.gsu.service.BaseServiceImpl;

public abstract class GoesUserServiceImpl<T extends GoesUser> extends BaseServiceImpl<T> implements GoesUserService<T> {
	private static final long serialVersionUID = 1L;

	public GoesUserServiceImpl(Class<T> entityClass) {
		super(entityClass);
	}

	@Override
	public T find(String email, String password) {
		try {
			return em.createQuery("SELECT o FROM " + entityClass.getSimpleName() + " o WHERE o.email=?1 AND o.password=?2", entityClass).setParameter(1, email)
					.setParameter(2, password).getSingleResult();
		} catch (EmptyResultDataAccessException e) {
			LOG.debug(e.getMessage(), e);
			return null;
		} catch (NoResultException e) {
			LOG.debug(e.getMessage(), e);
			return null;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
}
