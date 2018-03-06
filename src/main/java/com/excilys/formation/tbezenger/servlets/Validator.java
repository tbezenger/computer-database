package com.excilys.formation.tbezenger.servlets;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static com.excilys.formation.tbezenger.Strings.NAME_FIELD;
import static com.excilys.formation.tbezenger.Strings.INTRODUCED_FIELD;
import static com.excilys.formation.tbezenger.Strings.DISCONTINUED_FIELD;
import static com.excilys.formation.tbezenger.Strings.COMPANY_FIELD;

import javax.servlet.http.HttpServletRequest;

import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.exceptions.validator.CompanyFieldException;
import com.excilys.formation.tbezenger.exceptions.validator.DiscontinuedFieldException;
import com.excilys.formation.tbezenger.exceptions.validator.IntroducedFieldException;
import com.excilys.formation.tbezenger.exceptions.validator.NameFieldException;
import com.excilys.formation.tbezenger.services.CompanyService;

public class Validator {

	private static void validName(String name) throws NameFieldException {
		if (name.length() > 30) {
			throw new NameFieldException();
		}
	}

	private static void validIntroduced(String introduced) throws IntroducedFieldException {
		if (introduced == "") {
			throw new IntroducedFieldException();
		}
	}

	private static void validDiscontinued(String introduced, String discontinued) throws DiscontinuedFieldException {
		if (introduced == "") {
			return;
		}
		if (Date.valueOf(discontinued).after(Date.valueOf(introduced))) {
			return;
		}
		throw new DiscontinuedFieldException();
	}

	private static void validCompany(String companyId) throws CompanyFieldException {
		Company company = CompanyService.getInstance().get(Integer.parseInt(companyId)).orElse(new Company());
		if (company.getId() == 0) {
			throw new CompanyFieldException();
		}
	}

	public static Map<String, String> validation(HttpServletRequest request) {
		Map<String, String> errors = new HashMap<String, String>();
		try {
			Validator.validName(request.getParameter(NAME_FIELD));
			Validator.validIntroduced(request.getParameter(INTRODUCED_FIELD));
			Validator.validDiscontinued(request.getParameter(INTRODUCED_FIELD), request.getParameter(DISCONTINUED_FIELD));
			Validator.validCompany(request.getParameter(COMPANY_FIELD));

		} catch (NameFieldException e) {
			errors.put(NAME_FIELD, e.getMessage());

		} catch (IntroducedFieldException e) {
			errors.put(INTRODUCED_FIELD, e.getMessage());

		} catch (DiscontinuedFieldException e) {
			errors.put(DISCONTINUED_FIELD, e.getMessage());

		} catch (CompanyFieldException e) {
			errors.put(COMPANY_FIELD, e.getMessage());

		}

		return errors;
	}
}
