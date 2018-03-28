package com.excilys.formation.tbezenger.cdb.exceptions.validator;

import com.excilys.formation.tbezenger.cdb.Strings;

public class CompanyFieldException extends Exception {
	/**
	 */
	private static final long serialVersionUID = 1L;
	public CompanyFieldException() {
		super(Strings.INVALID_COMPANY);
	}

}
