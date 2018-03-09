package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	private ComputerPage page = new ComputerPage();;
	private ComputerService computerService = ComputerService.getInstance();
	private String search = "";
	private String orderBy = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		if (request.getParameter(ROWS) != null) {
			page.setRows(Integer.parseInt(request.getParameter(ROWS)));
		}
		if (request.getParameter(PAGE) != null) {
			page.setNumPage(Integer.parseInt(request.getParameter(PAGE)));

			if (page.getNumPage() > page.getTotalResults() / page.getRows() + 1) {
				page.setNumPage(page.getTotalResults() / page.getRows() + 1);
			}
			page.setNumPage(page.getNumPage());
			if (!search.equals("")) {
				if (!orderBy.equals("")) {
					page = computerService.getOrderedPage(page, orderBy, search);
				} else {
					page = computerService.getComputersPagebySearch(search, page.getRows(), page.getNumPage());
				}

			} else {
				if (!orderBy.equals("")) {
					page = computerService.getOrderedPage(page, orderBy, search);
				} else {
					page = computerService.getPage(page.getNumPage(), page.getRows());
				}
			}

		} else if (request.getParameter("search") != null) {
			page.setNumPage(1);
			search = request.getParameter("search");
			page = computerService.getComputersPagebySearch(search, page.getRows(), page.getNumPage());

		} else if (request.getParameter("orderBy") != null) {
			orderBy = request.getParameter("orderBy");
			System.out.println("orderby : " + orderBy);
			page = computerService.getOrderedPage(page, orderBy, search);

		} else {
			page.setNumPage(1);
			page = computerService.getPage(page.getNumPage(), page.getRows());
			search = "";
			orderBy = "";

		}
		request.setAttribute(COMPUTER_PAGE, page);
	    this.getServletContext().getRequestDispatcher(DASHBOARD_VIEW).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		for (String s : request.getParameter(SELECTION).split(SELECTION_SEPARATOR)) {
			computerService.delete(Integer.parseInt(s));
		}
		response.sendRedirect(DASHBOARD);
	}

}
