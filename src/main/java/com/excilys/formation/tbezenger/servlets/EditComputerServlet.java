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

	public String test = "testaj3";
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("testt", test);
	    this.getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(request, response);
	}

}
