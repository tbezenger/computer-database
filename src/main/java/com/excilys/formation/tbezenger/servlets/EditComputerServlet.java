package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.excilys.formation.tbezenger.Strings.COMPUTER;
import static com.excilys.formation.tbezenger.Strings.ID;
import static com.excilys.formation.tbezenger.Strings.COMPANIES;
import static com.excilys.formation.tbezenger.Strings.EDIT_COMPUTER_VIEW;
import static com.excilys.formation.tbezenger.Strings.NAME_FIELD;
import static com.excilys.formation.tbezenger.Strings.DISCONTINUED_FIELD;
import static com.excilys.formation.tbezenger.Strings.INTRODUCED_FIELD;
import static com.excilys.formation.tbezenger.Strings.COMPANY_FIELD;
import static com.excilys.formation.tbezenger.Strings.DASHBOARD;
import static com.excilys.formation.tbezenger.Strings.ERRORS;

@WebServlet("/editComputer")
public class EditComputerServlet extends HttpServlet {
	/**
	 */
	private static final long serialVersionUID = 1L;

	private WebAppModel model = new WebAppModel();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter(ID));
		request.setAttribute(COMPUTER, model.getComputer(id));
		request.setAttribute(COMPANIES, model.getCompanies());
	    this.getServletContext().getRequestDispatcher(EDIT_COMPUTER_VIEW).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> errors = Validator.validation(request);

		if (errors.isEmpty()) {
			model.editComputer(Integer.parseInt(request.getParameter(ID)), request.getParameter(NAME_FIELD),
							   request.getParameter(INTRODUCED_FIELD), request.getParameter(DISCONTINUED_FIELD),
							   Integer.parseInt(request.getParameter(COMPANY_FIELD)));
			response.sendRedirect(DASHBOARD);
		} else {
			request.setAttribute(ERRORS, errors);
			doGet(request, response);
		}
	}

}
