package com.excilys.formation.tbezenger.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.DTO.ComputerDTO;

@Component
public class ComputerDTOValidator implements Validator {

	@Autowired
	public static CompanyService companyService;

	@Override
	public boolean supports(Class<?> paramClass) {
		return ComputerDTO.class.equals(paramClass);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "introduced", "introduced.required");
		ComputerDTO computerDTO = (ComputerDTO) obj;
		if (computerDTO.getName().length() < 5) {
			errors.rejectValue("name", "name.short");
		} else if (computerDTO.getName().length() > 30) {
			errors.rejectValue("name", "name.long");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company", "company required");
	}

}
