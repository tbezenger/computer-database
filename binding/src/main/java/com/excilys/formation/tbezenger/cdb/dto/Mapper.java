package com.excilys.formation.tbezenger.cdb.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.excilys.formation.tbezenger.cdb.model.Company;
import com.excilys.formation.tbezenger.cdb.model.Computer;
import com.excilys.formation.tbezenger.cdb.model.ComputerPage;

public class Mapper {

	private Mapper() { }

	public static CompanyDTO toDTO(Company company) {
		CompanyDTO companyDTO = new CompanyDTO();
		if (company == null) {
			companyDTO.setId(0);
			companyDTO.setName("");
		} else {
			companyDTO.setId(company.getId());
			companyDTO.setName(company.getName());
		}
		return companyDTO;
	}

	public static Company toCompany(CompanyDTO companyDTO) {
		return new Company(companyDTO.getId(), companyDTO.getName());
	}


	public static ComputerDTO toDTO(Computer computer) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setId(computer.getId());
		computerDTO.setName(computer.getName());
		computerDTO.setIntroduced((computer.getIntroduced() != null) ? computer.getIntroduced().toString() : null);
		computerDTO.setDiscontinued((computer.getDiscontinued() != null) ? computer.getDiscontinued().toString() : null);
		computerDTO.setCompany(toDTO(computer.getCompany()));
		return computerDTO;
	}

	public static Computer toComputer(ComputerDTO computerDTO) {
		return new Computer(computerDTO.getId(),
							computerDTO.getName(),
							computerDTO.getIntroduced() != "" ? Date.valueOf(computerDTO.getIntroduced()) : null,
							computerDTO.getDiscontinued() != "" ? Date.valueOf(computerDTO.getDiscontinued()) : null,
							toCompany(computerDTO.getCompany()));
	}

	public static List<CompanyDTO> toCompanyDTOList(List<Company> companies) {
		List<CompanyDTO> companiesDTO = new ArrayList<>();
		for (Company company : companies) {
			companiesDTO.add(Mapper.toDTO(company));
		}
		return companiesDTO;
	}

	public static List<ComputerDTO> toComputerDTOList(List<Computer> computers) {
		List<ComputerDTO> computersDTO = new ArrayList<>();
		for (Computer computer : computers) {
			computersDTO.add(Mapper.toDTO(computer));
		}
		return computersDTO;
	}

	public static ComputerPageDTO toDTO(ComputerPage page) {
		ComputerPageDTO pageDTO = new ComputerPageDTO();
		pageDTO.setComputers(toComputerDTOList(page.getComputers()));
		pageDTO.setNumPage(page.getNumPage());
		pageDTO.setMaxPage(page.getMaxPage());
		pageDTO.setRows(page.getRows());
		pageDTO.setTotalResults(page.getTotalResults());
		pageDTO.setSearch(page.getSearch());
		pageDTO.setOrderBy(page.getOrderBy());
		pageDTO.setIsAscending(page.getIsAscending());
		return pageDTO;
	}
}