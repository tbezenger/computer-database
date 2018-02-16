package com.excilys.formation.tbezenger.services;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

public interface Service<T>{
	static Logger logger = Logger.getLogger("STDOUT");

	List<T> getAll();
	Optional<T> get(int id);
}
