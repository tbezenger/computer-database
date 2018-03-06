package com.excilys.formation.tbezenger.exceptions.DAO;

import static com.excilys.formation.tbezenger.Strings.PERSISTENCE_FAIL;

public class PersistException extends DatabaseException {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public PersistException() {
		super(PERSISTENCE_FAIL);
	}
}
