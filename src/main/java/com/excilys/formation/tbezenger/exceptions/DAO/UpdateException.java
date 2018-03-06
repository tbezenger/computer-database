package com.excilys.formation.tbezenger.exceptions.DAO;

import static com.excilys.formation.tbezenger.Strings.UPDATE_FAIL;

public class UpdateException extends DatabaseException {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public UpdateException() {
		super(UPDATE_FAIL);
	}

}
