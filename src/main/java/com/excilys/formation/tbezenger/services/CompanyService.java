package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.DAO.CompanyManager;
import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;

public class CompanyService implements Service<Company> {
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
	public Optional<Company> get(int id) {
		Optional<Company> company = Optional.ofNullable(new Company());
		try {
			company = CompanyManager.getInstance().findById(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return company;
	}

	@Override
	public List<Company> getAll() {
		List<Company> companies = new ArrayList<Company>();
		try {
			companies = CompanyManager.getInstance().findall();

		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return companies;
	}
}