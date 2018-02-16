package com.excilys.formation.tbezenger.GUI.CLI;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;


import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;
import com.excilys.formation.tbezenger.services.CompanyService;
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
	private static final String DELETED = "Suppression reussie";
	private static final String UPDATED = "Mise a jour reussie";
	private static final String CREATED = "Creation reussie";
	private static final String DONT_EXIST = "L'element demande n'existe pas";
	private static final String NULL = "null";
	private static final String SEPARATION_CHAR = "\\s";



	

	public static void launch() {
		Scanner scan = new Scanner(System.in);
		String[] parsedCommand = scan.nextLine().split(SEPARATION_CHAR);
		Computer computer;
		
		while (!parsedCommand[0].equals("quit")) {
			switch(parsedCommand[0]) {
				case "create":
					if (parsedCommand[1].equals(COMPUTER) && parsedCommand.length == 6) {
						computer = createComputer(parsedCommand);
						if (computer.getId()!=0) {
							System.out.println(CREATED +":"+ computer.toString());
						}
					}
					else
						System.out.println(NOT_RECOGNIZED);
					break;
					
					
				case "get":	
					if (parsedCommand[1].equals(COMPUTER) && parsedCommand.length == 3){
						// parsedCommand[2] = computer.id
						computer = ComputerService.getINSTANCE().get(Integer.parseInt(parsedCommand[2])).orElse(new Computer());
						if (computer.getId()!=0) {
							System.out.println("computer read :"+ computer.toString());
						}
						else {
							System.out.println(DONT_EXIST);
						}
					}	
					else
						System.out.println(NOT_RECOGNIZED);
					break;
				
				
					
				case "delete":
					if (parsedCommand[1].equals(COMPUTER) && parsedCommand.length == 3){
						// parsedCommand[2] = computer.id
						if (ComputerService.getINSTANCE().delete(Integer.parseInt(parsedCommand[2]))) {
							System.out.println(DELETED);
						}
					}	
					else
						System.out.println(NOT_RECOGNIZED);
					break;
					
					
					
				case "update":
					if (parsedCommand[1].equals(COMPUTER) && parsedCommand.length == 7) {
						updateComputer(parsedCommand);
					}
						
				
					else
						System.out.println(NOT_RECOGNIZED);
					
					break;
					
					
					
				case "getall":
					switch (parsedCommand[1]) {
						case COMPUTER:
							List<Computer> computers = ComputerService.getINSTANCE().getAll();
							for (Computer c : computers) {
								System.out.println(c);
							}
							break;
							
							
							
						case COMPANY:
							List<Company> companies = CompanyService.getINSTANCE().getAll();
							for (Company c : companies) {
								System.out.println(c);
							}
							break;
							
							
							
						default :
							System.out.println(NOT_RECOGNIZED);
							break;
							
					}
					break;
					
				case "help":
					System.out.println(HELPER);
					break;

					
				default:
					System.out.println(NOT_RECOGNIZED);
					break;
					
					
					
			}
			
			parsedCommand = scan.nextLine().split(SEPARATION_CHAR);
		}
		scan.close();
	}
	
	
	public static Computer createComputer(String[] parsedCommand){
		// parsedCommand[2] = computer.name         / parsedCommand[3] = computer.introduced
		// parsedCommand[4] = computer.discontinued / parsedCommand[5] = company.id
		Company company = CompanyService.getINSTANCE().get(Integer.parseInt(parsedCommand[5])).orElse(new Company());
		return ComputerService.getINSTANCE().create(new Computer(parsedCommand[2],
															Date.valueOf(parsedCommand[3]),
															parsedCommand[4].equals(NULL)? null : Date.valueOf(parsedCommand[4]),
															company));
	}
	
	
	public static void updateComputer(String[] parsedCommand) {
		//parsedCommand[2] = computer.id / parsedCommand[3] = computer.name / parsedCommand[4] = computer.introduced
		//parsedCommand[5] = computer.discontinued / parsedCommand[6] = company.id
								
		Company company = CompanyService.getINSTANCE().get(Integer.parseInt(parsedCommand[6])).orElse(new Company());
		if (company.getId()==0) {
			System.out.println(DONT_EXIST);
			return;
		}
		if(ComputerService.getINSTANCE().update(new Computer(Integer.parseInt(parsedCommand[2]),
										parsedCommand[3],
										Date.valueOf(parsedCommand[4]),
										parsedCommand[5].equals(NULL)? null : Date.valueOf(parsedCommand[5]),
										company))) {
		
			System.out.println(UPDATED);
		}
	}
}
