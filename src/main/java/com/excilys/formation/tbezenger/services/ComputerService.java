package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.model.Computer;
import com.excilys.formation.tbezenger.model.ComputerPage;

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

	public ComputerPage getPage(int numPage, int rowsByPage, String search, String orderBy, String order, boolean isAscending) {
		ComputerPage computerPage = new ComputerPage();
		try {
			computerPage = computerManager.findPage(numPage, rowsByPage, search, orderBy, order, isAscending);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computerPage;
	}
}