package com.excilys.formation.tbezenger.exceptions.DAO;

import static com.excilys.formation.tbezenger.Strings.GET_FAIL;

public class GetException extends DatabaseException {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public GetException() {
		super(GET_FAIL);
	}
}
