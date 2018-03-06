package com.excilys.formation.tbezenger.exceptions.DAO;

import static com.excilys.formation.tbezenger.Strings.DELETE_FAIL;

public class DeleteException extends DatabaseException {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public DeleteException() {
		super(DELETE_FAIL);
	}
}
