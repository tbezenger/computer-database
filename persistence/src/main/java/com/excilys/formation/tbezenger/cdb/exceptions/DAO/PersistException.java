package com.excilys.formation.tbezenger.cdb.exceptions.DAO;

import static com.excilys.formation.tbezenger.cdb.Strings.PERSISTENCE_FAIL;

public class PersistException extends DatabaseException {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public PersistException() {
		super(PERSISTENCE_FAIL);
	}
}
