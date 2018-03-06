package com.excilys.formation.tbezenger.Model;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ComputerPage {

	static Logger logger = LogManager.getLogger("STDOUT");

	private List<Computer> computers;
	private int numPage;

	public ComputerPage() { }

	public ComputerPage(List<Computer> computers, int numPage) {
		super();
		this.computers = computers;
		this.numPage = numPage;
	}
	public List<Computer> getComputers() {
		return computers;
	}
	public void setComputers(List<Computer> computers) {
		this.computers = computers;
	}

	public int getNumPage() {
		return numPage;
	}
	public void setNumPage(int numPage) {
		this.numPage = numPage;
	}
}
