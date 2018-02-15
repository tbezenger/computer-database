package com.excilys.formation.tbezenger.services;

import java.util.List;
import java.util.Optional;

public interface Service<T>{
	List<T> getAll();
	Optional<T> get(int id);
}
