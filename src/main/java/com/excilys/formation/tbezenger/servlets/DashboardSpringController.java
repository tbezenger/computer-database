package com.excilys.formation.tbezenger.servlets;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.excilys.formation.tbezenger.Strings.SELECTION;
import static com.excilys.formation.tbezenger.Strings.SELECTION_SEPARATOR;

import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.model.ComputerPage;
import com.excilys.formation.tbezenger.services.ComputerService;


@Controller
public class DashboardSpringController {

	@Autowired
	private ComputerService computerService;

	@GetMapping("dashboard")
	public String getDashboardPage(ModelMap model, @RequestParam Map<String, String> params) {
		try {
			int rowsByPage = params.get("rows") != null ? Integer.parseInt(params.get("rows")) : 10;
			int numPage = params.get("page") != null ? Integer.parseInt(params.get("page")) : 1;
			String search = params.get("search") != null ? params.get("search") : "";
			String orderBy = params.get("orderBy") != null ? params.get("orderBy") : "computer.id";
			ComputerPage page = computerService.getPage(numPage, rowsByPage, search, orderBy, "DESC", true);
			model.addAttribute("computerPage", page);
			return "dashboard";
		} catch (DatabaseException e) {
			return "500";
		}
	}

	@PostMapping("dashboard")
	public String deleteComputer(ModelMap model, @RequestParam Map<String, String> params) {
		try {
			for (String s : params.get(SELECTION).split(SELECTION_SEPARATOR)) {
				computerService.delete(Integer.parseInt(s));
			}
			return "dashboard";
		} catch (DatabaseException e) {
			return "500";
		}
	}
}