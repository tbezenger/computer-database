package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;
import java.util.Map;

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
	private static final String NAME_FIELD = "computerName";
	private static final String INTRODUCED_FIELD = "introduced";
	private static final String DISCONTINUED_FIELD = "discontinued";
	private static final String COMPANY_FIELD = "companyId";


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("companies", model.getCompanies());
	    this.getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> erreurs = Validator.validation(request);

		if (erreurs.isEmpty()) {
			model.createComputer(request.getParameter(NAME_FIELD), request.getParameter(INTRODUCED_FIELD),
					request.getParameter(DISCONTINUED_FIELD), Integer.parseInt(request.getParameter(COMPANY_FIELD)));
			response.sendRedirect("dashboard");

		} else {
			request.setAttribute("errors", erreurs);
			request.setAttribute("companies", model.getCompanies());
			this.getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);
		}

	}
}