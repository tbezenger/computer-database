package com.excilys.formation.tbezenger.GUI.CLI;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.excilys.formation.tbezenger.DTO.CompanyDTO;
import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.Model.ComputerPage;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerService;

public class CommandLineInterface {
	private static final String HELPER = "commandes disponibles : \n"
			+ "- create computer {name} {introduction date} {discontinuation date} {company id}\n"
			+ "- get computer {id}\n" + "- get page {page_number}\n" + "- getall computer\n" + "- getall company\n"
			+ "- delete computer {id}\n"
			+ "- update computer {id} {name} {introduction date} {discontinuation date} {company id}\n" + "- help\n"
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
	private static final String BYE = "Au revoir :'(\n";
	private static final String INCOMPATIBLE_DATES = "\"introduction date\" doit etre plus ancienne que \"discontinuation date\"";
	private static final String PAGE = "page";
	private static final String BAD_PAGE = "La page demandée n'existe pas";

	private static final Logger LOGGER = LogManager.getLogger(CommandLineInterface.class);

	public static void launch() {
		Scanner scan = new Scanner(System.in);
		String[] parsedCommand = scan.nextLine().split(SEPARATION_CHAR);
		ComputerDTO computerDTO;
		ComputerPage computerPage = new ComputerPage();

		while (!parsedCommand[0].equals("quit")) {
			switch (parsedCommand[0]) {
			case "create":
				if (parsedCommand.length == 6 && parsedCommand[1].equals(COMPUTER)) {
					try {
						computerDTO = createComputer(parsedCommand);
						if (computerDTO.getId() != 0) {
							LOGGER.info(CREATED + " : " + computerDTO.getName());
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
						computerDTO = ComputerService.getInstance().get(Integer.parseInt(parsedCommand[2]))
								.orElse(new ComputerDTO());
						if (computerDTO.getId() != 0) {
							LOGGER.info("Read computer : " + computerDTO.getName());
						} else {
							LOGGER.error(DONT_EXIST);
						}
					} catch (NumberFormatException e) {
						LOGGER.error(BAD_ID);
					}
				} else if (parsedCommand.length == 3 && parsedCommand[1].equals(PAGE)) {
					try {
						List<ComputerDTO> computersPage = computerPage.getPage(Integer.parseInt(parsedCommand[2]));
						if (computersPage.size() != 0) {
							for (ComputerDTO c : computersPage) {
								LOGGER.info(c.getName());
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
						if (ComputerService.getInstance().delete(Integer.parseInt(parsedCommand[2]))) {
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
						List<ComputerDTO> computersDTO = ComputerService.getInstance().getAll();
						for (ComputerDTO c : computersDTO) {
							LOGGER.info(c.getName());
						}
						break;

					case COMPANY:
						List<CompanyDTO> companiesDTO = CompanyService.getInstance().getAll();
						for (CompanyDTO c : companiesDTO) {
							LOGGER.info(c.getName());
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
	}

	public static ComputerDTO createComputer(String[] parsedCommand)
			throws NumberFormatException, IllegalArgumentException {
		// parsedCommand[2] = computer.name / parsedCommand[3] = computer.introduced
		// parsedCommand[4] = computer.discontinued / parsedCommand[5] = company.id
		if (Date.valueOf(parsedCommand[3]).before(Date.valueOf(parsedCommand[4]))) {
			CompanyDTO companyDTO = CompanyService.getInstance().get(Integer.parseInt(parsedCommand[5]))
					.orElse(new CompanyDTO());

			ComputerDTO computerDTO = new ComputerDTO();
			computerDTO.setName(parsedCommand[2]);
			computerDTO.setIntroduced(Date.valueOf(parsedCommand[3]));
			computerDTO.setDiscontinued(parsedCommand[4].equals(NULL) ? null : Date.valueOf(parsedCommand[4]));
			computerDTO.setCompany(companyDTO);

			return ComputerService.getInstance().create(computerDTO);
		} else {
			LOGGER.error(INCOMPATIBLE_DATES);
			return new ComputerDTO();
		}
	}

	public static void updateComputer(String[] parsedCommand) throws NumberFormatException, IllegalArgumentException {
		// parsedCommand[2] = computer.id / parsedCommand[3] = computer.name /
		// parsedCommand[4] = computer.introduced
		// parsedCommand[5] = computer.discontinued / parsedCommand[6] = company.id
		if (Date.valueOf(parsedCommand[4]).before(Date.valueOf(parsedCommand[5]))) {
			CompanyDTO companyDTO = CompanyService.getInstance().get(Integer.parseInt(parsedCommand[6]))
					.orElse(new CompanyDTO());
			if (companyDTO.getId() == 0) {
				LOGGER.error(DONT_EXIST);
				return;
			}
			ComputerDTO computerDTO = new ComputerDTO();
			computerDTO.setName(parsedCommand[2]);
			computerDTO.setIntroduced(Date.valueOf(parsedCommand[3]));
			computerDTO.setDiscontinued(parsedCommand[4].equals(NULL) ? null : Date.valueOf(parsedCommand[4]));
			computerDTO.setCompany(companyDTO);

			if (ComputerService.getInstance().update(computerDTO)) {
				LOGGER.info(UPDATED);
			}
		} else {
			LOGGER.error(INCOMPATIBLE_DATES);
			return;
		}
	}
}
