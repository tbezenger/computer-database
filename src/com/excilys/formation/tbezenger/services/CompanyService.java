package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.excilys.formation.tbezenger.DAO.CompanyManager;
import com.excilys.formation.tbezenger.Exceptions.DatabaseException;
import com.excilys.formation.tbezenger.Model.Company;

public class CompanyService implements Service<Company>{
	private static CompanyService INSTANCE;
	private CompanyService() {}
	
	public static CompanyService getINSTANCE() {
		if (INSTANCE==null) {
			INSTANCE=new CompanyService();
		}
		return INSTANCE;
	}

	@Override
	public Optional<Company> get(int id) {
		Optional<Company> company = Optional.ofNullable(new Company());
		try {
			company = CompanyManager.getINSTANCE().findById(id);
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
		return company;
	}

	@Override
	public List<Company> getAll() {
		List<Company> companies = new ArrayList<Company>();
		try {
			 companies = CompanyManager.getINSTANCE().findall();
			
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
		return companies;
	}	
}
