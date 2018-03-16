package com.excilys.formation.tbezenger.servlets;

import java.util.Map;
import static com.excilys.formation.tbezenger.Strings.COMPANIES;
import static com.excilys.formation.tbezenger.Strings.NAME_FIELD;
import static com.excilys.formation.tbezenger.Strings.DISCONTINUED_FIELD;
import static com.excilys.formation.tbezenger.Strings.INTRODUCED_FIELD;
import static com.excilys.formation.tbezenger.Strings.COMPANY_FIELD;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.DTO.Mapper;
import com.excilys.formation.tbezenger.model.Company;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerService;

@Controller
public class AddComputerSpringMVC {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;

	@GetMapping("addComputer")
	public String getAddComputerPage(ModelMap model) {
		model.addAttribute(COMPANIES, Mapper.toCompanyDTOList(companyService.getAll()));
		return ("addComputer");
	}


	@PostMapping("addComputer")
	public String addComputer(ModelMap model, @RequestParam Map<String, String> params) {
		if (createComputer(params.get(NAME_FIELD), params.get(INTRODUCED_FIELD), params.get(DISCONTINUED_FIELD), params.get(COMPANY_FIELD))) {
			return "redirect:dashboard";
		}
		return "addComputer";
	}


	public boolean createComputer(String name, String introduced, String discontinued, String companyId) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setName(name);
		computerDTO.setDiscontinued(discontinued);
		computerDTO.setIntroduced(introduced);
		computerDTO.setCompany(Mapper.toDTO(companyService.get(Integer.parseInt(companyId)).orElse(new Company())));
		return computerService.create(Mapper.toComputer(computerDTO));
	}
}