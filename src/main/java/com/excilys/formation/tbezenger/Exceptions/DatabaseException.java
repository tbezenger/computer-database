package com.excilys.formation.tbezenger.Exceptions;

public class DatabaseException extends Exception {
	private static final long serialVersionUID = 1L;
	public static String CONNECTION_FAIL = "Erreur lors de la connexion à la base de données";
	public static String PERSISTENCE_FAIL = "Erreur lors de la persitance";
	public static String UPDATE_FAIL = "Erreur lors de la mise à jour de données";
	public static String GET_FAIL = "Erreur lors de la récupération de données";
	public static String DELETE_FAIL = "Erreur lors de la suppression de données";

	public DatabaseException(String message){
		super(message);
	}

}
