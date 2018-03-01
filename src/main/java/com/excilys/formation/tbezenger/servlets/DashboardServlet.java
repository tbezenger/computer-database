package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;
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

	private int numPage = 1;
	private int rowsByPage = 10;


	public int getNumPage() {
		return numPage;
	}

	public void setNumPage(int numPage) {
		this.numPage = numPage;
	}

	public int getRowsByPage() {
		return rowsByPage;
	}

	public void setRowsByPage(int rowsByPage) {
		this.rowsByPage = rowsByPage;
	}

	public List<ComputerDTO> getComputerPage() {
		return model.getComputersPage(numPage, rowsByPage);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("computerPage", getComputerPage());
		request.setAttribute("computerCount", ComputerService.getInstance().getComputersNumber());

	    this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}
}
