package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.Exceptions.DatabaseException;

public class ComputerService implements Service<ComputerDTO> {

	private static ComputerService instance;

	private ComputerService() {
	}

	public static ComputerService getInstance() {
		if (instance == null) {
			instance = new ComputerService();
		}
		return instance;
	}

	public ComputerDTO create(ComputerDTO computerDTO) {
		try {
			computerDTO = ComputerManager.getInstance().persist(computerDTO);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computerDTO;
	}

	@Override
	public Optional<ComputerDTO> get(int id) {
		Optional<ComputerDTO> computerDTO = Optional.ofNullable(new ComputerDTO());
		try {
			computerDTO = ComputerManager.getInstance().findById(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computerDTO;
	}

	public boolean update(ComputerDTO computerDTO) {
		try {
			return ComputerManager.getInstance().update(computerDTO);
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
	public List<ComputerDTO> getAll() {
		List<ComputerDTO> computersDTO = new ArrayList<ComputerDTO>();
		try {
			computersDTO = ComputerManager.getInstance().findall();
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computersDTO;
	}

	public List<ComputerDTO> getPage(int numPage, int rowsByPage) {
		List<ComputerDTO> computersDTO = new ArrayList<ComputerDTO>();
		try {
			computersDTO = ComputerManager.getInstance().findPage(numPage - 1, rowsByPage);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return computersDTO;
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
}
