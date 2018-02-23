package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.DAO.CompanyManager;
import com.excilys.formation.tbezenger.DTO.CompanyDTO;
import com.excilys.formation.tbezenger.Exceptions.DatabaseException;

public class CompanyService implements Service<CompanyDTO> {
	private static CompanyService instance;

	private CompanyService() {
	}

	public static CompanyService getInstance() {
		if (instance == null) {
			instance = new CompanyService();
		}
		return instance;
	}

	@Override
	public Optional<CompanyDTO> get(int id) {
		Optional<CompanyDTO> companyDTO = Optional.ofNullable(new CompanyDTO());
		try {
			companyDTO = CompanyManager.getInstance().findById(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return companyDTO;
	}

	@Override
	public List<CompanyDTO> getAll() {
		List<CompanyDTO> companiesDTO = new ArrayList<CompanyDTO>();
		try {
			companiesDTO = CompanyManager.getInstance().findall();

		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return companiesDTO;
	}
}
