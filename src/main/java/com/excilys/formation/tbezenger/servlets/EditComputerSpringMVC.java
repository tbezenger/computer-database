package com.excilys.formation.tbezenger.servlets;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.DTO.Mapper;
import com.excilys.formation.tbezenger.model.Company;
import com.excilys.formation.tbezenger.model.Computer;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerService;

import static com.excilys.formation.tbezenger.Strings.COMPUTER;
import static com.excilys.formation.tbezenger.Strings.ID;
import static com.excilys.formation.tbezenger.Strings.COMPANIES;
import static com.excilys.formation.tbezenger.Strings.NAME_FIELD;
import static com.excilys.formation.tbezenger.Strings.DISCONTINUED_FIELD;
import static com.excilys.formation.tbezenger.Strings.INTRODUCED_FIELD;
import static com.excilys.formation.tbezenger.Strings.COMPANY_FIELD;

@Controller
public class EditComputerSpringMVC {

	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;

	@GetMapping("editComputer")
	public String getEditComputerPage(ModelMap model, @RequestParam Map<String, String> params) {
		model.addAttribute(COMPUTER, Mapper.toDTO(computerService.get(Integer.parseInt(params.get("id"))).orElse(new Computer())));
		model.addAttribute(COMPANIES, Mapper.toCompanyDTOList(companyService.getAll()));
		return ("editComputer");
	}

	@PostMapping("editComputer")
	public String addComputer(ModelMap model, @RequestParam Map<String, String> params) {
		if (editComputer(params.get(ID), params.get(NAME_FIELD), params.get(INTRODUCED_FIELD), params.get(DISCONTINUED_FIELD), params.get(COMPANY_FIELD))) {
			return "redirect:dashboard";
		}
		return "editComputer";
	}

	public boolean editComputer(String id, String name, String introduced, String discontinued, String companyId) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setId(Integer.parseInt(id));
		computerDTO.setName(name);
		computerDTO.setDiscontinued(discontinued);
		computerDTO.setIntroduced(introduced);
		computerDTO.setCompany(Mapper.toDTO(companyService.get(Integer.parseInt(companyId)).orElse(new Company())));
		return computerService.update(Mapper.toComputer(computerDTO));
	}
}
