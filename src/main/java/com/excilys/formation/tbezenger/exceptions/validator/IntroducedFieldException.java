package com.excilys.formation.tbezenger.exceptions.validator;

import static com.excilys.formation.tbezenger.Strings.INVALID_INTRODUCED;

public class IntroducedFieldException extends Exception {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public IntroducedFieldException() {
		super(INVALID_INTRODUCED);
	}
}
