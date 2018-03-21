package com.excilys.formation.tbezenger.servlets;

import static com.excilys.formation.tbezenger.Strings.COMPANIES;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.DTO.Mapper;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.model.Company;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerService;

@Controller
public class AddComputerSpringController {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	@Autowired
	private ComputerDTOValidator computerValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(computerValidator);
	}

	@GetMapping("addComputer")
	public String getAddComputerPage(ModelMap model) {
		try {
			model.addAttribute(COMPANIES, Mapper.toCompanyDTOList(companyService.getAll()));
			model.addAttribute("addForm", new ComputerDTO());
			return ("addComputer");
		} catch (DatabaseException e) {
			return "500";
		}
	}


	@PostMapping("addComputer")
	public String addComputer(@ModelAttribute("addForm") @Validated(ComputerDTO.class) ComputerDTO computerDTO, BindingResult bindingResult, ModelMap model) {
		try {
			if (!bindingResult.hasErrors()) {
				if (createComputer(computerDTO.getName(), computerDTO.getIntroduced(), computerDTO.getDiscontinued(), Integer.toString(computerDTO.getCompany().getId()))) {
					return "redirect:dashboard";
				}
			}
			model.addAttribute(COMPANIES, Mapper.toCompanyDTOList(companyService.getAll()));
			return "addComputer";
		} catch (DatabaseException e) {
			return "500";
		}
	}


	public boolean createComputer(String name, String introduced, String discontinued, String companyId) throws DatabaseException {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setName(name);
		computerDTO.setDiscontinued(discontinued);
		computerDTO.setIntroduced(introduced);
		computerDTO.setCompany(Mapper.toDTO(companyService.get(Integer.parseInt(companyId)).orElse(new Company())));
		return computerService.create(Mapper.toComputer(computerDTO));

	}
}