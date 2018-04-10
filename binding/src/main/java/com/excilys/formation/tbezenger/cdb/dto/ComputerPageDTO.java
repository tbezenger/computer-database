package com.excilys.formation.tbezenger.cdb.dto;

import java.util.List;


public class ComputerPageDTO {
	private List<ComputerDTO> computers;
	private int numPage = 1;
	private int maxPage;
	private int rows = 10;
	private long totalResults;
	private String search = "";
	private String orderBy = "";
	private boolean isAscending = true;

	public List<ComputerDTO> getComputers() {
		return computers;
	}
	public void setComputers(List<ComputerDTO> computerDTOs) {
		this.computers = computerDTOs;
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
	public long getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public boolean getIsAscending() {
		return isAscending;
	}
	public void setIsAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}
}
