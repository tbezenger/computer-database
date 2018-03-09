package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.Model.Computer;
import com.excilys.formation.tbezenger.Model.ComputerPage;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;

public class ComputerService implements Service<Computer> {

	private static ComputerService instance;

	private ComputerService() {
	}

	public static ComputerService getInstance() {
		if (instance == null) {
			instance = new ComputerService();
		}
		return instance;
	}

	public Computer create(Computer computer) {
		try {
			computer = ComputerManager.getInstance().persist(computer);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computer;
	}

	@Override
	public Optional<Computer> get(int id) {
		Optional<Computer> computer = Optional.ofNullable(new Computer());
		try {
			computer = ComputerManager.getInstance().findById(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computer;
	}

	public boolean update(Computer computer) {
		try {
			return ComputerManager.getInstance().update(computer);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	}

	public boolean delete(int id) {
		try {
			return ComputerManager.getInstance().remove(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Computer> getAll() {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			computers = ComputerManager.getInstance().findall();
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computers;
	}

	public ComputerPage getPage(int numPage, int rowsByPage) {
		ComputerPage computerPage = new ComputerPage();
		try {
			computerPage = ComputerManager.getInstance().findPage(numPage, rowsByPage);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computerPage;
	}

	public int getComputersNumber() {
		int computersNumber = 0;
		try {
			computersNumber = ComputerManager.getInstance().getComputersNumber();
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computersNumber;
	}

	public ComputerPage getComputersPagebySearch(String search, int rowsByPage, int numPage) {
		ComputerPage page = null;
		try {
			page = ComputerManager.getInstance().getComputersPageBySearch(search, rowsByPage, numPage);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return page;
	}

	public ComputerPage getOrderedPage(ComputerPage page, String orderBy, String search) {
		try {
			page = ComputerManager.getInstance().getOrderedPage(page, orderBy, search);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return page;
	}
}