package com.excilys.formation.tbezenger.exceptions.validator;

import static com.excilys.formation.tbezenger.Strings.INVALID_DISCONTINUED;

public class DiscontinuedFieldException extends Exception {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public DiscontinuedFieldException() {
		super(INVALID_DISCONTINUED);
	}

}
