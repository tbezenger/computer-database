package com.excilys.formation.tbezenger.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.Exceptions.DatabaseException;
import com.excilys.formation.tbezenger.Model.Computer;

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

	public List<Computer> getPage(int numPage, int rowsByPage) {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			computers = ComputerManager.getInstance().findPage(numPage - 1, rowsByPage);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computers;
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

	public ComputerDTO computerToDTO(Computer computer) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setId(computer.getId());
		computerDTO.setName(computer.getName());
		computerDTO.setIntroduced(computer.getIntroduced().toString());
		computerDTO.setDiscontinued(computer.getDiscontinued().toString());
		computerDTO.setCompany(CompanyService.getInstance().companyToDTO(computer.getCompany()));
		return computerDTO;
	}

	public Computer DTOToComputer(ComputerDTO computerDTO) {
		return new Computer(computerDTO.getId(), computerDTO.getName(), Date.valueOf(computerDTO.getIntroduced()),
							Date.valueOf(computerDTO.getDiscontinued()), CompanyService.getInstance().DTOToCompany(computerDTO.getCompany()));
	}

}