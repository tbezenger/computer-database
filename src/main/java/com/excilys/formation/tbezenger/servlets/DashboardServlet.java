package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		request.setAttribute("computerCount", ComputerService.getInstance().getComputersNumber());
		maxPages = ComputerService.getInstance().getComputersNumber() / rowsByPage + 1;

		if (request.getParameter("page") != null) {
			numPage = Integer.parseInt(request.getParameter("page"));
			model.getComputersFromNewPage(numPage, rowsByPage);
		} else {
			numPage = 1;
			model.getComputersFromNewPage(numPage, rowsByPage);
		}
		request.setAttribute("page", numPage);
		List<Integer> links = initLinkPages(model.getPage().getNumPage());
		request.setAttribute("links", links);
		request.setAttribute("computerPage", model.getPage().getComputers());
		request.setAttribute("computerCount", ComputerService.getInstance().getComputersNumber());
	    this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		for (String s : request.getParameter("selection").split(",")) {
			model.deleteComputer(Integer.parseInt(s));
		}
		response.sendRedirect("dashboard");
	}

}
