package tr.edu.gsu.service;

import java.io.Serializable;
import java.util.List;

import tr.edu.gsu.domain.BaseEntity;

public interface BaseService<T extends BaseEntity> extends Serializable {

	public List<T> findAll();

	public T find(Long id);

	public void save(T entity);

	public T update(T entity);

	public void delete(T entity);

	public void refresh(T entity);

	public void flush();
}
