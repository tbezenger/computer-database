package com.excilys.formation.tbezenger.cdb.exceptions.validator;

import static com.excilys.formation.tbezenger.cdb.Strings.INVALID_NAME;

public class NameFieldException extends Exception {
	/**
	 */
	private static final long serialVersionUID = 1L;

	public NameFieldException() {
		super(INVALID_NAME);
	}
}
