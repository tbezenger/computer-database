package com.excilys.formation.tbezenger.exceptions.validator;

import static com.excilys.formation.tbezenger.Strings.INVALID_COMPANY;

public class CompanyFieldException extends Exception {
	/**
	 */
	private static final long serialVersionUID = 1L;
	public CompanyFieldException() {
		super(INVALID_COMPANY);
	}

}
