package com.excilys.formation.tbezenger.services;

import java.util.List;
import java.util.Optional;

public interface Service<T>{
	
	T create(T t);
	Optional<T> get(int id);
	T update(T t);
	boolean delete(int id);
	List<T> getAll();
	List<T> getPage(int numPage);	
}
