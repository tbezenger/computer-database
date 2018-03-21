package com.excilys.formation.tbezenger.controllers;

import static com.excilys.formation.tbezenger.Strings.COMPANIES;
import static com.excilys.formation.tbezenger.Strings.COMPUTER;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.DTO.Mapper;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.model.Company;
import com.excilys.formation.tbezenger.model.Computer;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerService;

@Controller
public class ComputerSpringController {

	private ComputerService computerService;
	private CompanyService companyService;
	private ComputerDTOValidator computerValidator;

	private ComputerSpringController(ComputerService computerService, CompanyService companyService,
									 ComputerDTOValidator computerValidator) {
		this.computerService = computerService;
		this.companyService = companyService;
		this.computerValidator = computerValidator;
	}

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

	@GetMapping("editComputer")
	public String getEditComputerPage(ModelMap model, @RequestParam Map<String, String> params) {
		try {
			model.addAttribute(COMPUTER, Mapper.toDTO(computerService.get(Integer.parseInt(params.get("id"))).orElse(new Computer())));
			model.addAttribute(COMPANIES, Mapper.toCompanyDTOList(companyService.getAll()));
			model.addAttribute("editForm", new ComputerDTO());
			return ("editComputer");
		} catch (DatabaseException e) {
			return "500";
		}
	}

	@PostMapping("editComputer")
	public String addComputer(@ModelAttribute("editForm") @Validated(ComputerDTO.class) ComputerDTO computerDTO,
			BindingResult bindingResult, ModelMap model, @RequestParam Map<String, String> params) {
		try {
			if (!bindingResult.hasErrors()) {
				if (editComputer(computerDTO.getId(), computerDTO.getName(), computerDTO.getIntroduced(), computerDTO.getDiscontinued(), computerDTO.getCompany().getId())) {
					return "redirect:dashboard";
				}
			}
			model.addAttribute(COMPUTER, Mapper.toDTO(computerService.get(Integer.parseInt(params.get("id"))).orElse(new Computer())));
			model.addAttribute(COMPANIES, Mapper.toCompanyDTOList(companyService.getAll()));
			return "editComputer";
		} catch (DatabaseException e) {
			return "500";
		}
	}

	public boolean editComputer(int id, String name, String introduced, String discontinued, int companyId) throws DatabaseException {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setId(id);
		computerDTO.setName(name);
		computerDTO.setDiscontinued(discontinued);
		computerDTO.setIntroduced(introduced);
		computerDTO.setCompany(Mapper.toDTO(companyService.get(companyId).orElse(new Company())));
		return computerService.update(Mapper.toComputer(computerDTO));
	}
}
