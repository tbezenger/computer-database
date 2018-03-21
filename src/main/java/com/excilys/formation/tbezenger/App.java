package com.excilys.formation.tbezenger;

import com.excilys.formation.tbezenger.cli.CommandLineInterface;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;

public class App {
	public static void main(String[] args) {
		System.out.println("Entrez une commande (help pour afficher l'aide)");
		try {
			CommandLineInterface.launch();
		} catch (DatabaseException e) { }
	}
}