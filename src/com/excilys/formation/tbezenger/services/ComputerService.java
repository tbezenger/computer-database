package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.Exceptions.DatabaseException;
import com.excilys.formation.tbezenger.Model.Computer;

public class ComputerService implements Service<Computer>{
	
	private static ComputerService INSTANCE;
	private ComputerService() {}
	
	public static ComputerService getINSTANCE() {
		if (INSTANCE==null) {
			INSTANCE=new ComputerService();
		}
		return INSTANCE;
	}
	
	
	public Computer create(Computer computer) {
		try {
			computer = ComputerManager.getINSTANCE().persist(computer);
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
		return computer;
	}
	
	@Override
	public Optional<Computer> get(int id){
		Optional<Computer> computer = Optional.ofNullable(new Computer());
		try {
			computer = ComputerManager.getINSTANCE().findById(id);
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
		return computer;
	}

	
	public boolean update(Computer computer) {
		try {
			return ComputerManager.getINSTANCE().update(computer);
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
		return false;
	}


	public boolean delete(int id) {
		try {
			return ComputerManager.getINSTANCE().remove(id);
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Computer> getAll() {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			computers =  ComputerManager.getINSTANCE().findall();
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
		return computers;
	}


	public List<Computer> getPage(int numPage, int rowsByPage) {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			computers =  ComputerManager.getINSTANCE().findPage(numPage, rowsByPage);
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
		return computers;
	}
}
