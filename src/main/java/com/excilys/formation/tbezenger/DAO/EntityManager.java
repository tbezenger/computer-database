package com.excilys.formation.tbezenger.DAO;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;


public interface EntityManager<T> {

    Logger LOGGER = LogManager.getLogger("roll");

	List<T> findall() throws DatabaseException;

	Optional<T> findById(int id) throws DatabaseException;

}
