package com.excilys.formation.tbezenger.cdb.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.cdb.dao.ComputerDAO;
import com.excilys.formation.tbezenger.cdb.model.Computer;
import com.excilys.formation.tbezenger.cdb.model.ComputerPage;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;

@Service
public class ComputerService implements IService<Computer> {


	private ComputerDAO computerDAO;
	public ComputerService(ComputerDAO computerDAO) {
		this.computerDAO = computerDAO;
	}

	@Transactional
	public boolean create(Computer computer) throws DatabaseException {
		try {
			computerDAO.persist(computer);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
		return true;
	}

	@Override
	public Optional<Computer> get(int id) throws DatabaseException {
		Optional<Computer> computer = Optional.ofNullable(new Computer());
		try {
			computer = computerDAO.findById(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
		return computer;
	}

	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public boolean update(Computer computer) throws DatabaseException {
		try {
			return computerDAO.update(computer);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public boolean delete(int id) throws DatabaseException {
		try {
			return computerDAO.remove(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	@Override
	public List<Computer> getAll() throws DatabaseException {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			computers = computerDAO.findall();
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
		return computers;
	}

	@Transactional
	public ComputerPage getPage(ComputerPage page)
								throws DatabaseException {
		try {
			page.setTotalResults(computerDAO.findRelevantComputersCount(page.getSearch()));
			page.setMaxPage((int) ((page.getTotalResults() - 1) / page.getRows() + 1));
			page.setNumPage(page.getNumPage() <= page.getMaxPage() ? page.getNumPage() : page.getMaxPage());
			page.setComputers(computerDAO.findRelevantComputers(page));
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
		return page;
	}

	public boolean removeByCompanyId(int companyId) throws DatabaseException {
		try {
			return computerDAO.removeByCompanyId(companyId);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

}