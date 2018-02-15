package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.Utils;

public interface EntityManager<T> {
	
	default Connection openConnection() throws SQLException {
		return DriverManager.getConnection(Utils.url, Utils.dbName, Utils.dbPassword);
	}

    List<T> findall() throws Exception;

	
}
