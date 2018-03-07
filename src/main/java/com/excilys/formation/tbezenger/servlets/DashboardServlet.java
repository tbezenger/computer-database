package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.excilys.formation.tbezenger.Strings.COMPUTER_COUNT;
import static com.excilys.formation.tbezenger.Strings.ROWS;
import static com.excilys.formation.tbezenger.Strings.PAGE;
import static com.excilys.formation.tbezenger.Strings.COMPUTER_PAGE;
import static com.excilys.formation.tbezenger.Strings.DASHBOARD_VIEW;
import static com.excilys.formation.tbezenger.Strings.SELECTION;
import static com.excilys.formation.tbezenger.Strings.SELECTION_SEPARATOR;
import static com.excilys.formation.tbezenger.Strings.DASHBOARD;

import com.excilys.formation.tbezenger.Model.ComputerPage;
import com.excilys.formation.tbezenger.services.ComputerService;


@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	/**
	 */
	private static final long serialVersionUID = 1L;

	private ComputerPage page;
	private int rowsByPage = 10;
	private ComputerService computerService = ComputerService.getInstance();


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int numPage;
		int computerCount = computerService.getComputersNumber();
		if (request.getParameter(ROWS) != null) {
			rowsByPage = Integer.parseInt(request.getParameter(ROWS));
		}
		int maxPage = computerCount / rowsByPage + 1;
		if (request.getParameter(PAGE) != null) {
			numPage = Integer.parseInt(request.getParameter(PAGE));
			if (numPage > maxPage) {
				numPage = maxPage;
			}
			page = computerService.getPage(numPage, maxPage, rowsByPage);
		} else {
			numPage = 1;
			page = computerService.getPage(numPage, maxPage, rowsByPage);
		}
		request.setAttribute(COMPUTER_PAGE, page);
		request.setAttribute(COMPUTER_COUNT, computerCount);
	    this.getServletContext().getRequestDispatcher(DASHBOARD_VIEW).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		for (String s : request.getParameter(SELECTION).split(SELECTION_SEPARATOR)) {
			computerService.delete(Integer.parseInt(s));
		}
		response.sendRedirect(DASHBOARD);
	}

}
