package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.excilys.formation.tbezenger.Strings;
import com.excilys.formation.tbezenger.Exceptions.DatabaseException;

public interface EntityManager<T> {

    Logger LOGGER = LogManager.getLogger("roll");

	default Connection openConnection() throws DatabaseException {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(Strings.url, Strings.dbName, Strings.dbPassword);
		} catch (SQLException e) {
			throw (new DatabaseException(DatabaseException.CONNECTION_FAIL));
		} catch (ClassNotFoundException e) {
			throw (new DatabaseException(DatabaseException.CONNECTION_FAIL));
		}
		return conn;
	}

	List<T> findall() throws DatabaseException;

	Optional<T> findById(int id) throws DatabaseException;

}
