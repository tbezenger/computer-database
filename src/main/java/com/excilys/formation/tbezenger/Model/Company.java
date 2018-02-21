package com.excilys.formation.tbezenger.Model;

public class Company {
	private int id;
	private String name;
	
	public Company() {}
	
	public Company(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Company(String name) {
		super();
		this.name = name;
	}

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
		return "Company [id=" + id + ", name=" + name + "]";
	}
	
}
