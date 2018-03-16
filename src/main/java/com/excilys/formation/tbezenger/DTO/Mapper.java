package com.excilys.formation.tbezenger.DTO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.excilys.formation.tbezenger.model.Company;
import com.excilys.formation.tbezenger.model.Computer;

public class Mapper {

	public static CompanyDTO toDTO(Company company) {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setId(company.getId());
		companyDTO.setName(company.getName());
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
		return new Computer(computerDTO.getId(), computerDTO.getName(), Date.valueOf(computerDTO.getIntroduced()),
							Date.valueOf(computerDTO.getDiscontinued()), toCompany(computerDTO.getCompany()));
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
}