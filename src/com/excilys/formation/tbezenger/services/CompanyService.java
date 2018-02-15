package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
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
	public boolean update(Company company) {
		try {
			 return CompanyManager.getINSTANCE().update(company);
			
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return false;
	}

	@Override
	public boolean delete(int id) {
		try {
			 return CompanyManager.getINSTANCE().remove(id);
			
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return false;
	}

	@Override
	public List<Company> getAll() {
		List<Company> companies = new ArrayList<Company>();
		try {
			 companies = CompanyManager.getINSTANCE().findall();
			
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return companies;
	}

	@Override
	public List<Company> getPage(int numPage) {
		List<Company> companies = new ArrayList<Company>();
		try {
			 companies = CompanyManager.getINSTANCE().findPage(numPage);
		} catch (Exception e) {
			// TODO logger
			System.out.println("error");
		}
		return companies;
	}
	
}
