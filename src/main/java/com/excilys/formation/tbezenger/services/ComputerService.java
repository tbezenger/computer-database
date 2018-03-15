package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.Model.Computer;
import com.excilys.formation.tbezenger.Model.ComputerPage;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;

@Service
public class ComputerService implements IService<Computer> {


	private ComputerManager computerManager;
	@Autowired
	public ComputerService(ComputerManager computerManager) {
		this.computerManager = computerManager;
	}


	public boolean create(Computer computer) {
		try {
			computerManager.persist(computer);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return true;
	}

	@Override
	public Optional<Computer> get(int id) {
		Optional<Computer> computer = Optional.ofNullable(new Computer());
		try {
			computer = computerManager.findById(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computer;
	}

	public boolean update(Computer computer) {
		try {
			return computerManager.update(computer);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	}

	public boolean delete(int id) {
		try {
			return computerManager.remove(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Computer> getAll() {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			computers = computerManager.findall();
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computers;
	}

	public ComputerPage getPage(int numPage, int rowsByPage) {
		ComputerPage computerPage = new ComputerPage();
		try {
			computerPage = computerManager.findPage(numPage, rowsByPage);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computerPage;
	}

	public int getComputersNumber() {
		int computersNumber = 0;
		try {
			computersNumber = computerManager.getComputersNumber();
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computersNumber;
	}

	public ComputerPage getComputersPagebySearch(String search, int rowsByPage, int numPage) {
		ComputerPage page = null;
		try {
			page = computerManager.getComputersPageBySearch(search, rowsByPage, numPage);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return page;
	}

	public ComputerPage getOrderedPage(ComputerPage page, String orderBy, String search) {
		try {
			page = computerManager.getOrderedPage(page, orderBy, search);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return page;
	}
}