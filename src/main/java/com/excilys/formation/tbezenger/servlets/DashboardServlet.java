package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.excilys.formation.tbezenger.Strings.COMPUTER_COUNT;
import static com.excilys.formation.tbezenger.Strings.ROWS;
import static com.excilys.formation.tbezenger.Strings.PAGE;
import static com.excilys.formation.tbezenger.Strings.LINKS;
import static com.excilys.formation.tbezenger.Strings.COMPUTER_PAGE;
import static com.excilys.formation.tbezenger.Strings.DASHBOARD_VIEW;
import static com.excilys.formation.tbezenger.Strings.SELECTION;
import static com.excilys.formation.tbezenger.Strings.SELECTION_SEPARATOR;
import static com.excilys.formation.tbezenger.Strings.DASHBOARD;

import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.services.ComputerService;


@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	/**
	 */
	private static final long serialVersionUID = 1L;

	private WebAppModel model = new WebAppModel();
	private int maxPages;
	private int rowsByPage = 10;
	private int numPage;

	public int getRowsByPage() {
		return rowsByPage;
	}

	public void setRowsByPage(int rowsByPage) {
		this.rowsByPage = rowsByPage;
	}

	public List<ComputerDTO> getComputersFromNewPage(int numPage) {
		return model.getComputersFromNewPage(numPage, rowsByPage);
	}

	public List<Integer> initLinkPages(int numPage) {
		List<Integer> linkPages = new ArrayList<Integer>();
		linkPages.add(1);
		for (int i = -2; i < 3; i++) {
			if (numPage + i > 1 && numPage + i < maxPages) {
				linkPages.add(numPage + i);
			}
		}
		linkPages.add(maxPages);
		return linkPages;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute(COMPUTER_COUNT, ComputerService.getInstance().getComputersNumber());
		if (request.getParameter(ROWS) != null) {
			rowsByPage = Integer.parseInt(request.getParameter(ROWS));
		}
		maxPages = ComputerService.getInstance().getComputersNumber() / rowsByPage + 1;

		if (request.getParameter(PAGE) != null) {
			numPage = Integer.parseInt(request.getParameter(PAGE));
			model.getComputersFromNewPage(numPage, rowsByPage);
		} else {
			numPage = 1;
			model.getComputersFromNewPage(numPage, rowsByPage);
		}
		request.setAttribute(PAGE, numPage);
		List<Integer> links = initLinkPages(model.getPage().getNumPage());
		request.setAttribute(LINKS, links);
		request.setAttribute(COMPUTER_PAGE, model.getPage().getComputers());
		request.setAttribute(COMPUTER_COUNT, ComputerService.getInstance().getComputersNumber());
	    this.getServletContext().getRequestDispatcher(DASHBOARD_VIEW).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		for (String s : request.getParameter(SELECTION).split(SELECTION_SEPARATOR)) {
			model.deleteComputer(Integer.parseInt(s));
		}
		response.sendRedirect(DASHBOARD);
	}

}
