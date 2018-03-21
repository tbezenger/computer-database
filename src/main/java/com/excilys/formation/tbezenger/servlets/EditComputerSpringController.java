package com.excilys.formation.tbezenger.servlets;

import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;


import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.DTO.Mapper;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.model.Company;
import com.excilys.formation.tbezenger.model.Computer;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerService;

import static com.excilys.formation.tbezenger.Strings.COMPUTER;
import static com.excilys.formation.tbezenger.Strings.COMPANIES;

@Controller
public class EditComputerSpringController {
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