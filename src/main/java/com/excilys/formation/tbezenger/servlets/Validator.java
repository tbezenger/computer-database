package com.excilys.formation.tbezenger.servlets;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.services.CompanyService;

public class Validator {

	private static final String NAME_FIELD = "computerName";
	private static final String INTRODUCED_FIELD = "introduced";
	private static final String DISCONTINUED_FIELD = "discontinued";
	private static final String COMPANY_FIELD = "companyId";

	private static final String NAME_ERROR = "Nom invalide";
	private static final String INTRODUCED_ERROR = "Date de lancement invalide";
	private static final String DISCONTINUED_ERROR = "Date d'arret invalide";
	private static final String COMPANY_ERROR = "Entreprise invalide";

	private static void validName(String name) throws Exception {
		if (name.length() > 30) {
			throw new Exception(NAME_ERROR);
		}
	}

	private static void validIntroduced(String introduced) throws Exception {
		if (introduced == "") {
			throw new Exception(INTRODUCED_ERROR);
		}
	}

	private static void validDiscontinued(String introduced, String discontinued) throws Exception {
		if (introduced == "") {
			return;
		}
		if (discontinued != "") {
			if (Date.valueOf(discontinued).after(Date.valueOf(introduced))) {
				return;
			}
		}
		throw new Exception(DISCONTINUED_ERROR);
	}

	private static void validCompany(String companyId) throws Exception {
		Company company = CompanyService.getInstance().get(Integer.parseInt(companyId)).orElse(new Company());
		if (company.getId() == 0) {
			throw new Exception(COMPANY_ERROR);
		}
	}

	public static Map<String, String> validation(HttpServletRequest request) {
		Map<String, String> erreurs = new HashMap<String, String>();
		try {
			Validator.validName(request.getParameter(NAME_FIELD));
		} catch (Exception e) {
			erreurs.put(NAME_FIELD, e.getMessage());
		}

		try {
			Validator.validIntroduced(request.getParameter(INTRODUCED_FIELD));
		} catch (Exception e) {
			erreurs.put(INTRODUCED_FIELD, e.getMessage());
		}

		try {
			Validator.validDiscontinued(request.getParameter(INTRODUCED_FIELD), request.getParameter(DISCONTINUED_FIELD));
		} catch (Exception e) {
			erreurs.put(DISCONTINUED_FIELD, e.getMessage());
		}

		try {
			Validator.validCompany(request.getParameter(COMPANY_FIELD));
		} catch (Exception e) {
			erreurs.put(COMPANY_FIELD, e.getMessage());
		}

		return erreurs;
	}
}
