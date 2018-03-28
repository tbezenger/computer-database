package com.excilys.formation.tbezenger.cdb.cli;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.cdb.model.Company;
import com.excilys.formation.tbezenger.cdb.model.Computer;
import com.excilys.formation.tbezenger.cdb.model.ComputerPage;
import com.excilys.formation.tbezenger.cdb.services.CompanyService;
import com.excilys.formation.tbezenger.cdb.services.ComputerService;
import com.excilys.formation.tbezenger.cdb.springconfig.ApplicationConfig;

public class CommandLineInterface {
	private static final String HELPER = "commandes disponibles : \n"
			+ "- create computer {name} {introduction date} {discontinuation date} {company id}\n"
			+ "- get computer {id}\n" + "- get page {page_number}\n" + "- getall computer\n" + "- getall company\n"
			+ "- delete computer {id}\n"
			+ "- delete company {id}\n"
			+ "- update computer {id} {name} {introduction date} {discontinuation date} {company id}\n" + "- help\n"
			+ "- quit";

	private static final int ROWSBYPAGE = 20;
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
	private static final String BYE = "Au revoir :'(\n";
	private static final String INCOMPATIBLE_DATES = "\"introduction date\" doit etre plus ancienne que \"discontinuation date\"";
	private static final String PAGE = "page";
	private static final String BAD_PAGE = "La page demandée n'existe pas";

	private static final Logger LOGGER = LogManager.getLogger(CommandLineInterface.class);


	private static ComputerService computerService;
	private static CompanyService companyService;

	public static void launch() throws DatabaseException {
	    AbstractApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

	    computerService = context.getBean(ComputerService.class);
	    companyService = context.getBean(CompanyService.class);

		Scanner scan = new Scanner(System.in);
		String[] parsedCommand = scan.nextLine().split(SEPARATION_CHAR);
		Computer computer;
		ComputerPage computerPage;

		while (!parsedCommand[0].equals("quit")) {
			switch (parsedCommand[0]) {
			case "create":
				if (parsedCommand.length == 6 && parsedCommand[1].equals(COMPUTER)) {
					try {
						if (createComputer(parsedCommand)) {
							LOGGER.info(CREATED);
						}
					} catch (NumberFormatException e) {
						LOGGER.error(BAD_ID);
					} catch (IllegalArgumentException e) {
						LOGGER.error(BAD_DATE);
					}
				} else {
					LOGGER.error(NOT_RECOGNIZED);
				}
				break;

			case "get":
				if (parsedCommand.length == 3 && parsedCommand[1].equals(COMPUTER)) {
					// parsedCommand[2] = computer.id
					try {
						computer = computerService.get(Integer.parseInt(parsedCommand[2]))
								.orElse(new Computer());
						if (computer.getId() != 0) {
							LOGGER.info("Read computer : " + computer.toString());
						} else {
							LOGGER.error(DONT_EXIST);
						}
					} catch (NumberFormatException e) {
						LOGGER.error(BAD_ID);
					}
				} else if (parsedCommand.length == 3 && parsedCommand[1].equals(PAGE)) {
					try {
						computerPage = computerService.getPage(Integer.parseInt(parsedCommand[2]), ROWSBYPAGE, "", "", true);
						List<Computer> computers = computerPage.getComputers();
						if (computers.size() != 0) {
							for (Computer c : computers) {
								LOGGER.info(c.toString());
							}
						} else {
							LOGGER.error(BAD_PAGE);
						}
					} catch (NumberFormatException e) {
						LOGGER.error(BAD_PAGE);
					}
				} else {
					LOGGER.error(NOT_RECOGNIZED);
				}
				break;

			case "delete":
				if (parsedCommand.length == 3 && parsedCommand[1].equals(COMPUTER)) {
					// parsedCommand[2] = computer.id
					try {
						if (computerService.delete(Integer.parseInt(parsedCommand[2]))) {
							LOGGER.info(DELETED);
						}
					} catch (NumberFormatException e) {
						LOGGER.error(BAD_ID);
					}
				} else if (parsedCommand[1].equals(COMPANY)) {
					// parsedCommand[2] = computer.id
					try {
						if (companyService.delete(Integer.parseInt(parsedCommand[2]))) {
							LOGGER.info(DELETED);
						}
					} catch (NumberFormatException e) {
						LOGGER.error(BAD_ID);
					}
				} else {
					LOGGER.error(NOT_RECOGNIZED);
				}
				break;

			case "update":
				if (parsedCommand.length == 7 && parsedCommand[1].equals(COMPUTER)) {
					try {
						updateComputer(parsedCommand);
					} catch (NumberFormatException e) {
						LOGGER.error(BAD_ID);
					} catch (IllegalArgumentException e) {
						LOGGER.error(BAD_DATE);
					}
				} else {
					LOGGER.error(NOT_RECOGNIZED);
				}

				break;

			case "getall":
				if (parsedCommand.length == 2) {
					switch (parsedCommand[1]) {
					case COMPUTER:
						List<Computer> computers = computerService.getAll();
						for (Computer c : computers) {
							LOGGER.info(c.getName() + " id : " + Integer.toString(c.getId()));
						}
						break;

					case COMPANY:
						List<Company> companies = companyService.getAll();
						for (Company c : companies) {
							LOGGER.info(c.getName() + " id : " + Integer.toString(c.getId()));
						}
						break;

					default:
						LOGGER.error(NOT_RECOGNIZED);
						break;

					}
					break;
				}

			case "help":
				LOGGER.info(HELPER);
				break;

			default:
				LOGGER.error(NOT_RECOGNIZED);
				break;

			}

			parsedCommand = scan.nextLine().split(SEPARATION_CHAR);
		}
		LOGGER.info(BYE);
		scan.close();
		context.close();
	}

	public static boolean createComputer(String[] parsedCommand)
			throws NumberFormatException, IllegalArgumentException, DatabaseException {
		// parsedCommand[2] = computer.name / parsedCommand[3] = computer.introduced
		// parsedCommand[4] = computer.discontinued / parsedCommand[5] = company.id
		if (Date.valueOf(parsedCommand[3]).before(Date.valueOf(parsedCommand[4]))) {
			Company company = companyService.get(Integer.parseInt(parsedCommand[5]))
					.orElse(new Company());
			return computerService.create(new Computer(parsedCommand[2], Date.valueOf(parsedCommand[3]),
					parsedCommand[4].equals(NULL) ? null : Date.valueOf(parsedCommand[4]), company));
		} else {
			LOGGER.error(INCOMPATIBLE_DATES);
			return false;
		}
	}

	public static void updateComputer(String[] parsedCommand) throws NumberFormatException, IllegalArgumentException, DatabaseException {
		// parsedCommand[2] = computer.id / parsedCommand[3] = computer.name /
		// parsedCommand[4] = computer.introduced
		// parsedCommand[5] = computer.discontinued / parsedCommand[6] = company.id
		if (Date.valueOf(parsedCommand[4]).before(Date.valueOf(parsedCommand[5]))) {
			Company company = companyService.get(Integer.parseInt(parsedCommand[6]))
					.orElse(new Company());
			if (company.getId() == 0) {
				LOGGER.error(DONT_EXIST);
				return;
			}
			if (computerService.update(new Computer(Integer.parseInt(parsedCommand[2]), parsedCommand[3],
							Date.valueOf(parsedCommand[4]),
							parsedCommand[5].equals(NULL) ? null : Date.valueOf(parsedCommand[5]), company))) {

				LOGGER.info(UPDATED);
			}
		} else {
			LOGGER.error(INCOMPATIBLE_DATES);
			return;
		}
	}
}