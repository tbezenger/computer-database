package com.excilys.formation.tbezenger.cdb.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.cdb.dao.CompanyDAO;
import org.springframework.stereotype.Service;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.cdb.model.Company;

@Service
public class CompanyService implements IService<Company> {

	private CompanyDAO companyManager;
	public CompanyService(CompanyDAO companyManager) {
		this.companyManager = companyManager;
	}


	@Override
	public Optional<Company> get(int id) throws DatabaseException {
		Optional<Company> company = Optional.ofNullable(new Company());
		try {
			company = companyManager.findById(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
		return company;
	}

	@Override
	public List<Company> getAll() throws DatabaseException {
		List<Company> companies = new ArrayList<Company>();
		try {
			companies = companyManager.findall();

		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
		return companies;
	}

	public boolean delete(int id) throws DatabaseException {
		try {
			return companyManager.remove(id);

		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
}