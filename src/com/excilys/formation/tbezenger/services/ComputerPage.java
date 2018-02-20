package com.excilys.formation.tbezenger.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.Exceptions.DatabaseException;
import com.excilys.formation.tbezenger.Model.Computer;

public class ComputerPage {
	private static ComputerPage INSTANCE;
	public static ComputerPage getINSTANCE(){
		if (INSTANCE==null) {
			INSTANCE = new ComputerPage();
		}
		return INSTANCE;
	}
	
	private ComputerPage() {}
	
	static Logger logger = LogManager.getLogger("STDOUT");
		
	private List<Computer> currentPage;
	private int numCurrentPage;
	private int rowsByPage = 20;
	private int pagesNumber;
	
	
	public List<Computer> getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(List<Computer> currentPage) {
		this.currentPage = currentPage;
	}
	public int getNumCurrentPage() {
		return numCurrentPage;
	}
	public void setNumCurrentPage(int numCurrentPage) {
		this.numCurrentPage = numCurrentPage;
	}
	public int getRowsByPage() {
		return rowsByPage;
	}
	public void setRowsByPage(int rowsByPage) {
		this.rowsByPage = rowsByPage;
	}
	public int getPagesNumber() {
		
		return pagesNumber;
	}
	public void setPagesNumber(int pagesNumber) {
		this.pagesNumber = pagesNumber;
	}
	
	
	public List<Computer> getPage(int numPage) {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			computers =  ComputerManager.getINSTANCE().findPage(numPage-1, rowsByPage);
			numCurrentPage = numPage;
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
		return computers;
	}

}
