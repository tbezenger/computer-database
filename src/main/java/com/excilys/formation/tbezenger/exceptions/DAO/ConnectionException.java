package com.excilys.formation.tbezenger.exceptions.DAO;

import static com.excilys.formation.tbezenger.Strings.CONNECTION_FAIL;

public class ConnectionException extends DatabaseException {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public ConnectionException() {
		super(CONNECTION_FAIL);
	}
}
