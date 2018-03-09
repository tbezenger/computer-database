package com.excilys.formation.tbezenger.Model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ComputerPage {

	static Logger logger = LogManager.getLogger("STDOUT");

	private List<Computer> computers = new ArrayList<Computer>();
	private int numPage = 1;
	private int maxPage;
	private int rows = 10;
	private int totalResults;

	public ComputerPage() { }

	public ComputerPage(List<Computer> computers, int numPage, int maxPage, int rows) {
		this.computers = computers;
		this.numPage = numPage;
		this.maxPage = maxPage;
		this.rows = rows;
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
	public int getMaxPage() {
		return maxPage;
	}
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}
}
