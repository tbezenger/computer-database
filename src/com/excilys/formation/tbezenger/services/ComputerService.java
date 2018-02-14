package com.excilys.formation.tbezenger.services;

import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.DAO.CompanyManager;
import com.excilys.formation.tbezenger.DAO.ComputerManager;
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
	@Override
	public Computer create(Computer computer) {
		try {
			computer = ComputerManager.getINSTANCE().persist(computer);
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return computer;
	}

	@Override
	public Optional<Computer> get(int id) {
		Optional<Computer> computer = Optional.ofNullable(new Computer());
		try {
			computer = ComputerManager.getINSTANCE().findById(id);
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return computer;
	}

	@Override
	public Computer update(Computer t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Computer> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Computer> getPage(int numPage) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
