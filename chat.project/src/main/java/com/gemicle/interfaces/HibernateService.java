package com.gemicle.interfaces;

import java.util.List;

public interface HibernateService<T> {
	public long save(T t);

	public void delete(T t);

	public T get(long id);
	
	public List<T> getList();
}
