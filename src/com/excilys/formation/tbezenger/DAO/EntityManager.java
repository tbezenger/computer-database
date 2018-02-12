package com.excilys.formation.tbezenger.DAO;

import java.util.List;

public interface EntityManager<T> {
		
    T find(int id) throws Exception;

    List<T> findall() throws Exception;

    boolean persist(T t) throws Exception;

    boolean remove(int id) throws Exception;

    boolean update(T t) throws Exception;
	
}
