package com.excilys.formation.tbezenger.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.DTO.Mapper;
import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerService;

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
	private CompanyService companyService = CompanyService.getInstance();
	private ComputerService computerService = ComputerService.getInstance();


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter(ID));
		request.setAttribute(COMPUTER, Mapper.toDTO(computerService.get(id).orElse(new Computer())));
		request.setAttribute(COMPANIES, Mapper.toCompanyDTOList(companyService.getAll()));
	    this.getServletContext().getRequestDispatcher(EDIT_COMPUTER_VIEW).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> errors = Validator.validation(request);

		if (errors.isEmpty()) {
			editComputer(Integer.parseInt(request.getParameter(ID)), request.getParameter(NAME_FIELD),
							   request.getParameter(INTRODUCED_FIELD), request.getParameter(DISCONTINUED_FIELD),
							   Integer.parseInt(request.getParameter(COMPANY_FIELD)));
			response.sendRedirect(DASHBOARD);
		} else {
			request.setAttribute(ERRORS, errors);
			doGet(request, response);
		}
	}

	public void editComputer(int id, String name, String introduced, String discontinued, int companyId) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setId(id);
		computerDTO.setName(name);
		computerDTO.setDiscontinued(discontinued);
		computerDTO.setIntroduced(introduced);
		computerDTO.setCompany(Mapper.toDTO(companyService.get(companyId).orElse(new Company())));
		computerService.update(Mapper.toComputer(computerDTO));
	}

}
