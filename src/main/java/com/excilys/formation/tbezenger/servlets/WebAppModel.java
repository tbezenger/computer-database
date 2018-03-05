package com.excilys.formation.tbezenger.servlets;

import java.util.ArrayList;
import java.util.List;

import com.excilys.formation.tbezenger.DTO.CompanyDTO;
import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.DTO.Mapper;
import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;
import com.excilys.formation.tbezenger.Model.ComputerPage;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerService;

public class WebAppModel {
	private ComputerPage page;
	private List<CompanyDTO> companiesDTO;
	private CompanyService companyService = CompanyService.getInstance();
	private ComputerService computerService = ComputerService.getInstance();

	public ComputerPage getPage() {
		return page;
	}
	public void setPage(ComputerPage page) {
		this.page = page;
	}
	public List<CompanyDTO> getCompaniesDTO() {
		return companiesDTO;
	}
	public void setCompaniesDTO(List<CompanyDTO> companiesDTO) {
		this.companiesDTO = companiesDTO;
	}

	public List<CompanyDTO> getCompanies() {
		List<CompanyDTO> companies = new ArrayList<>();
		for (Company company : companyService.getAll()) {
			companies.add(Mapper.toDTO(company));
		}
		return companies;
	}

	public List<ComputerDTO> getComputersFromNewPage(int numPage, int rowsByPage) {
		page = ComputerService.getInstance().getPage(numPage, rowsByPage);
		return getComputers();
	}

	public void createComputer(String name, String introduced, String discontinued, int companyId) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setName(name);
		computerDTO.setDiscontinued(discontinued);
		computerDTO.setIntroduced(introduced);
		computerDTO.setCompany(getCompany(companyId));
		computerService.create(Mapper.toComputer(computerDTO));
	}

	public void editComputer(int id, String name, String introduced, String discontinued, int companyId) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setId(id);
		computerDTO.setName(name);
		computerDTO.setDiscontinued(discontinued);
		computerDTO.setIntroduced(introduced);
		computerDTO.setCompany(getCompany(companyId));
		computerService.update(Mapper.toComputer(computerDTO));
	}

	public void deleteComputer(int id) {
		computerService.delete(id);
	}

	private CompanyDTO getCompany(int id) {
		return Mapper.toDTO(companyService.get(id).orElse(new Company()));
	}

	public ComputerDTO getComputer(int id) {
		return Mapper.toDTO(computerService.get(id).orElse(new Computer()));
	}

	public List<ComputerDTO> getComputers() {
		List<ComputerDTO> computers = new ArrayList<>();
		for (Computer computer : page.getComputers()) {
			computers.add(Mapper.toDTO(computer));
		}
		return computers;
	}
}
