package com.excilys.formation.tbezenger.DTO;

import java.sql.Date;

import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;

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
		computerDTO.setIntroduced(computer.getIntroduced().toString());
		computerDTO.setDiscontinued(computer.getDiscontinued().toString());
		computerDTO.setCompany(toDTO(computer.getCompany()));
		return computerDTO;
	}

	public static Computer toComputer(ComputerDTO computerDTO) {
		return new Computer(computerDTO.getId(), computerDTO.getName(), Date.valueOf(computerDTO.getIntroduced()),
							Date.valueOf(computerDTO.getDiscontinued()),toCompany(computerDTO.getCompany()));
	}
}
