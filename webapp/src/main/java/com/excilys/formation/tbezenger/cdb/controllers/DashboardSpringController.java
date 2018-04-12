package com.excilys.formation.tbezenger.cdb.controllers;

import java.security.Principal;
import java.util.Map;


import com.excilys.formation.tbezenger.cdb.Strings;
import com.excilys.formation.tbezenger.cdb.dto.Mapper;
import com.excilys.formation.tbezenger.cdb.model.ComputerPage;
import com.excilys.formation.tbezenger.cdb.services.ComputerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
			String orderBy = params.get("orderBy") != null ? params.get("orderBy") : "id";
			boolean isAscending = params.get("isAscending") != null ? Boolean.valueOf(params.get("isAscending")) : true;
			ComputerPage page = new ComputerPage(numPage, rowsByPage, search, orderBy, isAscending);
			page = computerService.getPage(page);
			model.addAttribute("page", Mapper.toDTO(page));
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
			return getDashboardPage(model, params);
		} catch (DatabaseException e) {
			return "500";
		}
	}
	
	
	  @GetMapping("/")
	  public String index(Model model, Principal principal) {
	    model.addAttribute("message", "You are logged in as " + principal.getName());
	    return "redirect:dashboard";
	  }


	@GetMapping("/login")
	public String login(ModelMap model, @RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "logout", required = false) String logout) {
	  if (error != null) {
		  System.out.println("noob");
		model.addAttribute("error", "Invalid username and password!");
		
	  }

	  if (logout != null) {
		  System.out.println("gg");
		model.addAttribute("msg", "You've been logged out successfully.");
	  }
	  return "login";

	}
	
	
}