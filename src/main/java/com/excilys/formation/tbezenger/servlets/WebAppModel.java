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
	private ComputerDTO currentComputer;
	private List<ComputerDTO> computersDTO;
	private List<CompanyDTO> companiesDTO;
	private CompanyService companyService = CompanyService.getInstance();
	private ComputerService computerService = ComputerService.getInstance();

	public ComputerDTO getCurrentComputer() {
		return currentComputer;
	}
	public void setCurrentComputer(ComputerDTO currentComputer) {
		this.currentComputer = currentComputer;
	}
	public List<ComputerDTO> getComputersDTO() {
		return computersDTO;
	}
	public void setComputersDTO(List<ComputerDTO> computersDTO) {
		this.computersDTO = computersDTO;
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

	public List<ComputerDTO> getComputersPage(int numPage, int rowsByPage) {
		ComputerPage computerPage =  new ComputerPage();
		List<ComputerDTO> computers = new ArrayList<>();
		for (Computer computer : computerPage.getPage(1)) {
			computers.add(Mapper.toDTO(computer));
		}
		return computers;
	}

	public void createComputer(String name, String discontinued, String introduced, int companyId) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setName(name);
		computerDTO.setDiscontinued(discontinued);
		computerDTO.setIntroduced(introduced);
		computerDTO.setCompany(Mapper.toDTO(companyService.get(companyId).orElse(new Company())));
		computerService.create(Mapper.toComputer(computerDTO));
	}

	public void editComputer(int id, String name, String discontinued, String introduced, int companyId) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setId(id);
		computerDTO.setName(name);
		computerDTO.setDiscontinued(discontinued);
		computerDTO.setIntroduced(introduced);
		computerDTO.setCompany(Mapper.toDTO(companyService.get(companyId).orElse(new Company())));
		computerService.update(Mapper.toComputer(computerDTO));
	}

	public void deleteComputer(int id) {

	}

	public ComputerDTO getComputer(int id) {
		setCurrentComputer(Mapper.toDTO(computerService.get(id).orElse(new Computer())));
		return currentComputer;
	}
}
