package com.excilys.formation.tbezenger.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface Service<T> {
	Logger LOGGER = LogManager.getLogger("STDOUT");

	List<T> getAll();

	Optional<T> get(int id);
}
