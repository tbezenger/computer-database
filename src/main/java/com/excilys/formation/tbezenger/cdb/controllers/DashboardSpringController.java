package com.excilys.formation.tbezenger.cdb.controllers;

import java.util.Map;


import com.excilys.formation.tbezenger.cdb.Strings;
import com.excilys.formation.tbezenger.cdb.model.ComputerPage;
import com.excilys.formation.tbezenger.cdb.services.ComputerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;


@Controller
public class DashboardSpringController {

	private ComputerService computerService;
	private DashboardSpringController(ComputerService computerService) {
		this.computerService = computerService;
	}

	@GetMapping("dashboard")
	public String getDashboardPage(ModelMap model, @RequestParam Map<String, String> params) {
		try {
			int rowsByPage = params.get("rows") != null ? Integer.parseInt(params.get("rows")) : 10;
			int numPage = params.get("page") != null ? Integer.parseInt(params.get("page")) : 1;
			String search = params.get("search") != null ? params.get("search") : "";
			String orderBy = params.get("orderBy") != null ? params.get("orderBy") : "computer.id";
			boolean isAscending = params.get("isAscending") != null ? Boolean.valueOf(params.get("isAscending")) : true;
			ComputerPage page = computerService.getPage(numPage, rowsByPage, search, orderBy, isAscending);
			model.addAttribute("computerPage", page);
            System.out.println(computerService.get(55));
            return "dashboard";
		} catch (DatabaseException e) {
			return "500";
		}
	}

	@PostMapping("dashboard")
	public String deleteComputer(ModelMap model, @RequestParam Map<String, String> params) {
		try {
			for (String s : params.get(Strings.SELECTION).split(Strings.SELECTION_SEPARATOR)) {
				computerService.delete(Integer.parseInt(s));
			}
			return "dashboard";
		} catch (DatabaseException e) {
			return "500";
		}
	}
}