package com.excilys.formation.tbezenger.GUI.CLI;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerPage;
import com.excilys.formation.tbezenger.services.ComputerService;

public class CommandLineInterface {
	private static final String HELPER = "commandes disponibles : \n"
			+ "- create computer {name} {introduction date} {discontinuation date} {company id}\n"
			+ "- get computer {id}\n"
			+ "- getall computer\n"
			+ "- getall company\n"
			+ "- delete computer {id}\n"
			+ "- update computer {id} {name} {introduction date} {discontinuation date} {company id}\n"
			+ "- help\n"
			+ "- quit";

	private static final String COMPUTER = "computer";
	private static final String COMPANY = "company";
	private static final String NOT_RECOGNIZED = "Commande non reconnue";
	private static final String DELETED = "Suppression réussie";
	private static final String UPDATED = "Mise à jour réussie";
	private static final String CREATED = "Création réussie";
	private static final String DONT_EXIST = "L'élément demandé n'existe pas";
	private static final String NULL = "null";
	private static final String SEPARATION_CHAR = "\\s";
	private static final String BAD_ID = "L'id entré n'est pas un numéro";
	private static final String BAD_DATE = "Mauvais format de date (utiliser aaaa-mm-jj)";
	private static final String BYE = "Good bye\n";
	private static final String INCOMPATIBLE_DATES = "\"introduction date\" doit etre plus ancienne que \"discontinuation date\"";
	private static final String PAGE = "page";
	private static final String BAD_PAGE = "La page demandée n'existe pas";


	private static final Logger logger = Logger.getLogger("STDOUT");
	

	public static void launch() {
		Scanner scan = new Scanner(System.in);
		String[] parsedCommand = scan.nextLine().split(SEPARATION_CHAR);
		Computer computer;
		
		while (!parsedCommand[0].equals("quit")) {
			switch(parsedCommand[0]) {
				case "create":
					if (parsedCommand[1].equals(COMPUTER) && parsedCommand.length == 6) {
						try{
							computer = createComputer(parsedCommand);
							if (computer.getId()!=0) {
								logger.info(CREATED +":"+ computer.toString());
							}
						}catch (NumberFormatException e) {
							logger.error(BAD_ID);
						}catch (IllegalArgumentException e) {
							logger.error(BAD_DATE);
						}
					}
					else
						logger.error(NOT_RECOGNIZED);
					break;
					
					
				case "get":	
					if (parsedCommand[1].equals(COMPUTER) && parsedCommand.length == 3){
						// parsedCommand[2] = computer.id
						try {
							computer = ComputerService.getINSTANCE().get(Integer.parseInt(parsedCommand[2])).orElse(new Computer());
							if (computer.getId()!=0) {
								logger.info("Read computer :"+ computer.toString());
							}
							else {
								logger.error(DONT_EXIST);
							}
						}catch (NumberFormatException e) {
							logger.error(BAD_ID);
						}
					}
					else if (parsedCommand[1].equals(PAGE) && parsedCommand.length == 3){
						try {
							List<Computer> computersPage = ComputerPage.getINSTANCE().getPage(Integer.parseInt(parsedCommand[2]));
							if (computersPage.size()!=0) {
								for (Computer c:computersPage) {
									logger.info(c.toString());
								}
							}
							else {
								logger.error(BAD_PAGE);
							}
						}catch (NumberFormatException e) {
							logger.error(BAD_PAGE);
						}
					}

					else
						logger.error(NOT_RECOGNIZED);
					break;
				
				
					
				case "delete":
					if (parsedCommand[1].equals(COMPUTER) && parsedCommand.length == 3){
						// parsedCommand[2] = computer.id
						try {
							if (ComputerService.getINSTANCE().delete(Integer.parseInt(parsedCommand[2]))) {
								logger.info(DELETED);
							}
						}catch (NumberFormatException e) {
							logger.error(BAD_ID);
						}
					}	
					else
						logger.error(NOT_RECOGNIZED);
					break;
					
					
					
				case "update":
					if (parsedCommand[1].equals(COMPUTER) && parsedCommand.length == 7) {
						try {
							updateComputer(parsedCommand);
						}catch (NumberFormatException e) {
							logger.error(BAD_ID);
						}catch (IllegalArgumentException e) {
							logger.error(BAD_DATE);
						}
					}
						
				
					else
						logger.error(NOT_RECOGNIZED);
					
					break;
					
					
					
				case "getall":
					switch (parsedCommand[1]) {
						case COMPUTER:
							List<Computer> computers = ComputerService.getINSTANCE().getAll();
							for (Computer c : computers) {
								logger.info(c.getName());
							}
							break;
							
							
						case COMPANY:
							List<Company> companies = CompanyService.getINSTANCE().getAll();
							for (Company c : companies) {
								logger.info(c.getName());
							}
							break;
							
							
						default :
							logger.error(NOT_RECOGNIZED);
							break;
							
					}
					break;
					
				case "help":
					logger.info(HELPER);
					break;

					
				default:
					logger.error(NOT_RECOGNIZED);
					break;
					
					
			}
			
			parsedCommand = scan.nextLine().split(SEPARATION_CHAR);
		}
		logger.info(BYE);
		scan.close();
	}
	
	
	public static Computer createComputer(String[] parsedCommand)throws NumberFormatException,IllegalArgumentException {
		// parsedCommand[2] = computer.name         / parsedCommand[3] = computer.introduced
		// parsedCommand[4] = computer.discontinued / parsedCommand[5] = company.id
		if (Date.valueOf(parsedCommand[3]).before(Date.valueOf(parsedCommand[4]))) {
			Company company = CompanyService.getINSTANCE().get(Integer.parseInt(parsedCommand[5])).orElse(new Company());
			return ComputerService.getINSTANCE().create(new Computer(parsedCommand[2],
																Date.valueOf(parsedCommand[3]),
																parsedCommand[4].equals(NULL)? null : Date.valueOf(parsedCommand[4]),
																company));
		}
		else {
			logger.error(INCOMPATIBLE_DATES);
			return new Computer();
		}
	}
	
	
	public static void updateComputer(String[] parsedCommand)throws NumberFormatException,IllegalArgumentException {
		//parsedCommand[2] = computer.id / parsedCommand[3] = computer.name / parsedCommand[4] = computer.introduced
		//parsedCommand[5] = computer.discontinued / parsedCommand[6] = company.id
		if (Date.valueOf(parsedCommand[4]).before(Date.valueOf(parsedCommand[5]))) {
			Company company = CompanyService.getINSTANCE().get(Integer.parseInt(parsedCommand[6])).orElse(new Company());
			if (company.getId()==0) {
				logger.error(DONT_EXIST);
				return;
			}
			if(ComputerService.getINSTANCE().update(new Computer(Integer.parseInt(parsedCommand[2]),
											parsedCommand[3],
											Date.valueOf(parsedCommand[4]),
											parsedCommand[5].equals(NULL)? null : Date.valueOf(parsedCommand[5]),
											company))) {
			
				logger.info(UPDATED);
			}
		}
		else {
			logger.error(INCOMPATIBLE_DATES);
			return;
		}
	}
}
