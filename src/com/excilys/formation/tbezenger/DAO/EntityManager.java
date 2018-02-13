package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import com.excilys.formation.tbezenger.Utils;

public interface EntityManager<T> {
	
	default Connection openConnection() throws Exception {
		return DriverManager.getConnection(Utils.url, Utils.dbName, Utils.dbPassword);
	}
		
    T find(int id) throws Exception;

    List<T> findall() throws Exception;

    T persist(T t) throws Exception;

    boolean remove(int id) throws Exception;

    boolean update(T t) throws Exception;
	
}
