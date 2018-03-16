package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.formation.tbezenger.DAO.CompanyManager;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.model.Company;

@Service
public class CompanyService implements IService<Company> {

	private CompanyManager companyManager;
	@Autowired
	public CompanyService(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}


	@Override
	public Optional<Company> get(int id) {
		Optional<Company> company = Optional.ofNullable(new Company());
		try {
			company = companyManager.findById(id);
		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return company;
	}

	@Override
	public List<Company> getAll() {
		List<Company> companies = new ArrayList<Company>();
		try {
			companies = companyManager.findall();

		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
		}
		return companies;
	}

	public boolean delete(int id) {
		try {
			return companyManager.remove(id);

		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}
}