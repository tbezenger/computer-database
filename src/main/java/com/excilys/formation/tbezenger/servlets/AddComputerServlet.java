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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.DTO.Mapper;
import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerService;

@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;

	/**
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute(COMPANIES, Mapper.toCompanyDTOList(companyService.getAll()));
	    this.getServletContext().getRequestDispatcher(ADD_COMPUTER_VIEW).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> erreurs = Validator.validation(request);

		if (erreurs.isEmpty()) {
			createComputer(request.getParameter(NAME_FIELD), request.getParameter(INTRODUCED_FIELD),
					request.getParameter(DISCONTINUED_FIELD), Integer.parseInt(request.getParameter(COMPANY_FIELD)));
			response.sendRedirect(DASHBOARD);

		} else {
			request.setAttribute(ERRORS, erreurs);
			doGet(request, response);
		}
	}

	public void createComputer(String name, String introduced, String discontinued, int companyId) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setName(name);
		computerDTO.setDiscontinued(discontinued);
		computerDTO.setIntroduced(introduced);
		computerDTO.setCompany(Mapper.toDTO(companyService.get(companyId).orElse(new Company())));
		computerService.create(Mapper.toComputer(computerDTO));
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	    AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
	    autowireCapableBeanFactory.autowireBean(this);
	}
}