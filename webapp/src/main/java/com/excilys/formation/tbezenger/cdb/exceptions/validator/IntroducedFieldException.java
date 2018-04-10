package com.excilys.formation.tbezenger.cdb.exceptions.validator;

import com.excilys.formation.tbezenger.cdb.Strings;

public class IntroducedFieldException extends Exception {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public IntroducedFieldException() {
		super(Strings.INVALID_INTRODUCED);
	}
}
