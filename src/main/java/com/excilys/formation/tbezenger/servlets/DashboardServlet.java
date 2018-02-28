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
import com.excilys.formation.tbezenger.DTO.Mapper;
import com.excilys.formation.tbezenger.Model.Computer;
import com.excilys.formation.tbezenger.Model.ComputerPage;
import com.excilys.formation.tbezenger.services.ComputerService;


@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	/**
	 */
	private static final long serialVersionUID = 1L;


	public List<ComputerDTO> getComputerPage() {
		ComputerPage computerPage =  new ComputerPage();
		List<ComputerDTO> computersDTO = new ArrayList<>();
		for (Computer computer : computerPage.getPage(1)) {
			computersDTO.add(Mapper.toDTO(computer));
		}
		return computersDTO;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("computerPage", getComputerPage());
		request.setAttribute("computerCount", ComputerService.getInstance().getComputersNumber());

	    this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}
}
