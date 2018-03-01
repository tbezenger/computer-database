package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet {
	private WebAppModel model = new WebAppModel();
	/**
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("companies", model.getCompanies());
	    this.getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);
	}
}
