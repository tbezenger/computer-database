package com.excilys.formation.tbezenger.DTO;

import java.sql.Date;

public class ComputerDTO {
	private int id;
	private String name;
	private Date introduced;
	private Date discontinued;
	private CompanyDTO company;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getIntroduced() {
		return introduced;
	}
	public void setIntroduced(Date introduced) {
		this.introduced = introduced;
	}
	public Date getDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(Date discontinued) {
		this.discontinued = discontinued;
	}
	public CompanyDTO getCompany() {
		return company;
	}
	public void setCompany(CompanyDTO company) {
		this.company = company;
	}
}
