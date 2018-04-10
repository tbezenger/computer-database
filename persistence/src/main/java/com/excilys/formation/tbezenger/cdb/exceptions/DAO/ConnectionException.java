package com.excilys.formation.tbezenger.cdb.exceptions.DAO;

import static com.excilys.formation.tbezenger.cdb.Strings.CONNECTION_FAIL;

public class ConnectionException extends DatabaseException {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public ConnectionException() {
		super(CONNECTION_FAIL);
	}
}
