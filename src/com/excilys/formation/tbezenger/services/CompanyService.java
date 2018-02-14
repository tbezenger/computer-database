package com.excilys.formation.tbezenger.services;

import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.DAO.CompanyManager;
import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;

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
	public Company create(Company company) {
		try {
			 company = CompanyManager.getINSTANCE().persist(company);
			
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return company;
	}
	
	@Override
	public Optional<Company> get(int id) {
		Optional<Company> company = Optional.ofNullable(new Company());
		try {
			company = CompanyManager.getINSTANCE().findById(id);
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return company;
	}

	@Override
	public Company update(Company t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Company> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Company> getPage(int numPage) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
