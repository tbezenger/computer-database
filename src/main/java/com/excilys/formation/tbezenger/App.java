package com.excilys.formation.tbezenger;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Repository;

import com.excilys.formation.tbezenger.GUI.CLI.CommandLineInterface;
import com.excilys.formation.tbezenger.springConfig.ApplicationAnnotationConfig;
import com.excilys.formation.tbezenger.springConfig.ApplicationConfig;

public class App {
	public static void main(String[] args) {
		System.out.println("Entrez une commande (help pour afficher l'aide)");
		CommandLineInterface.launch();
	}
}