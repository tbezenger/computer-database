package com.excilys.formation.tbezenger.cdb.exceptions.validator;

import static com.excilys.formation.tbezenger.cdb.Strings.INVALID_DISCONTINUED;

public class DiscontinuedFieldException extends Exception {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public DiscontinuedFieldException() {
		super(INVALID_DISCONTINUED);
	}

}
