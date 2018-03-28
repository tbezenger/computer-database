package com.excilys.formation.tbezenger.cdb.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;

public interface IService<T> {
	Logger LOGGER = LogManager.getLogger("STDOUT");

	List<T> getAll() throws DatabaseException;

	Optional<T> get(int id) throws DatabaseException;
}
