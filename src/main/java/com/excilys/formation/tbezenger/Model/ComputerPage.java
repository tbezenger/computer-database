package com.excilys.formation.tbezenger.Model;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.services.ComputerService;

public class ComputerPage {

	public ComputerPage() {
	}

	static Logger logger = LogManager.getLogger("STDOUT");

	private List<ComputerDTO> currentPage;
	private int numCurrentPage;
	private int rowsByPage = 20;
	private int pagesNumber;

	public List<ComputerDTO> getCurrentPage() {
		return currentPage;
	}

	public int getNumCurrentPage() {
		return numCurrentPage;
	}

	public int getRowsByPage() {
		return rowsByPage;
	}

	public void setRowsByPage(int rowsByPage) {
		this.rowsByPage = rowsByPage;
	}

	public int getPagesNumber() {
		this.pagesNumber = ComputerService.getInstance().getComputersNumber() / rowsByPage;
		return this.pagesNumber;
	}

	public void setPagesNumber() {

	}

	public List<ComputerDTO> getPage(int numPage) {
		this.currentPage = ComputerService.getInstance().getPage(numPage, rowsByPage);
		this.numCurrentPage = numPage;
		return this.currentPage;
	}

}
