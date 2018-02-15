package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.Utils;
import com.excilys.formation.tbezenger.Exceptions.DatabaseException;

public interface EntityManager<T> {
	
	default Connection openConnection() throws DatabaseException{
		Connection conn=null;
		try{
			conn = DriverManager.getConnection(Utils.url, Utils.dbName, Utils.dbPassword);
		}catch (SQLException e) {
			throw (new DatabaseException(DatabaseException.CONNECTION_FAIL));
		}
		return conn;
	}

    List<T> findall() throws DatabaseException;
    Optional<T> findById(int id) throws DatabaseException;


	
}
