package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
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
	public boolean update(Computer computer) {
		try {
			return ComputerManager.getINSTANCE().update(computer);
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return false;
	}


	@Override
	public boolean delete(int id) {
		try {
			return ComputerManager.getINSTANCE().remove(id);
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return false;
	}

	@Override
	public List<Computer> getAll() {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			computers =  ComputerManager.getINSTANCE().findall();
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return computers;
	}

	@Override
	public List<Computer> getPage(int numPage) {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			computers =  ComputerManager.getINSTANCE().findPage(numPage);
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return computers;
	}
}
