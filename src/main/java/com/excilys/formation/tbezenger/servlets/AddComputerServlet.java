package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;
import java.util.Map;
import static com.excilys.formation.tbezenger.Strings.ADD_COMPUTER_VIEW;
import static com.excilys.formation.tbezenger.Strings.COMPANIES;
import static com.excilys.formation.tbezenger.Strings.NAME_FIELD;
import static com.excilys.formation.tbezenger.Strings.DISCONTINUED_FIELD;
import static com.excilys.formation.tbezenger.Strings.INTRODUCED_FIELD;
import static com.excilys.formation.tbezenger.Strings.COMPANY_FIELD;
import static com.excilys.formation.tbezenger.Strings.DASHBOARD;
import static com.excilys.formation.tbezenger.Strings.ERRORS;

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
		request.setAttribute(COMPANIES, model.getCompanies());
	    this.getServletContext().getRequestDispatcher(ADD_COMPUTER_VIEW).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> erreurs = Validator.validation(request);

		if (erreurs.isEmpty()) {
			model.createComputer(request.getParameter(NAME_FIELD), request.getParameter(INTRODUCED_FIELD),
					request.getParameter(DISCONTINUED_FIELD), Integer.parseInt(request.getParameter(COMPANY_FIELD)));
			response.sendRedirect(DASHBOARD);

		} else {
			request.setAttribute(ERRORS, erreurs);
			doGet(request, response);
		}

	}
}