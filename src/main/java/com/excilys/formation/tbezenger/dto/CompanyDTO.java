package com.excilys.formation.tbezenger.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CompanyDTO {
	private int id;

	@NotNull
	@Size(min = 5, max = 5)
	private String name;

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
	@Override
	public String toString() {
		return "CompanyDTO [id=" + id + ", name=" + name + "]";
	}
}
