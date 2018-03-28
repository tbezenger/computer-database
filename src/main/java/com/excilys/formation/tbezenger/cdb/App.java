package com.excilys.formation.tbezenger.cdb;

import com.excilys.formation.tbezenger.cdb.cli.CommandLineInterface;
import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;

public class App {
	public static void main(String[] args) {
		System.out.println("Entrez une commande (help pour afficher l'aide)");
		try {
			CommandLineInterface.launch();
		} catch (DatabaseException e) { }
	}
}