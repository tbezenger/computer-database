package com.excilys.formation.tbezenger.Model;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.excilys.formation.tbezenger.services.ComputerService;

public class ComputerPage {

	static Logger logger = LogManager.getLogger("STDOUT");

	private List<Computer> computers;
	private int numPage;


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
