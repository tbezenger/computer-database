package com.excilys.formation.tbezenger.cdb.controllers;

import java.sql.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.excilys.formation.tbezenger.cdb.dto.ComputerDTO;

@Component
public class ComputerDTOValidator implements Validator {

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
		if (computerDTO.getCompany().getId() == 0) {
			errors.rejectValue("company.id", "company.required");
		}
		if (!computerDTO.getIntroduced().isEmpty() && !computerDTO.getDiscontinued().isEmpty()) {
			if (Date.valueOf(computerDTO.getIntroduced()).after(Date.valueOf(computerDTO.getDiscontinued()))) {
				errors.rejectValue("discontinued", "discontinued.before");
			}
		}
	}
}
