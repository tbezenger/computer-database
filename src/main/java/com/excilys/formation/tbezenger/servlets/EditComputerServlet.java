package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/editComputer")
public class EditComputerServlet extends HttpServlet {
	/**
	 */
	private static final long serialVersionUID = 1L;
	private WebAppModel model = new WebAppModel();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("computer", model.getComputer(id));
		request.setAttribute("companies", model.getCompanies());
	    this.getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		model.editComputer(Integer.parseInt(request.getParameter("id")), request.getParameter("computerName"),
						   request.getParameter("introduced"), request.getParameter("discontinued"),
						   Integer.parseInt(request.getParameter("companyId")));

	}

}
