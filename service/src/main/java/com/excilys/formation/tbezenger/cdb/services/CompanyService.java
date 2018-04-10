package com.excilys.formation.tbezenger.cdb.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.cdb.dao.CompanyDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.cdb.model.Company;

@Service
public class CompanyService implements IService<Company> {

	private CompanyDAO companyDAO;
	private ComputerService computerService;
	public CompanyService(CompanyDAO companyDAO, ComputerService computerService) {
		this.companyDAO = companyDAO;
		this.computerService = computerService;
	}


	@Override
	public Optional<Company> get(int id) throws DatabaseException {
		Optional<Company> company = Optional.ofNullable(new Company());
		try {
			company = companyDAO.findById(id);
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
			companies = companyDAO.findall();

		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
		return companies;
	}

	@Transactional
	public boolean delete(int id) throws DatabaseException {
		try {
			computerService.removeByCompanyId(id);
			return companyDAO.remove(id);

		} catch (DatabaseException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
}