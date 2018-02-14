package com.excilys.formation.tbezenger.GUI.CLI;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import com.excilys.formation.tbezenger.DAO.CompanyManager;
import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;
import com.excilys.formation.tbezenger.services.CompanyService;
import com.excilys.formation.tbezenger.services.ComputerService;

public class CommandLineInterface {
	public static void launch() {
		Scanner scan = new Scanner(System.in);
		String[] parsedCommand = scan.nextLine().split("\\s");
		Company company;
		Computer computer;
		
		while (!parsedCommand[0].equals("quit")) {
			switch(parsedCommand[0]) {
				case "create":
					if (parsedCommand[1].equals("computer")) {
						// parsedCommand[2] = computer.name
						// parsedCommand[3] = computer.introduced
						// parsedCommand[4] = computer.discontinued
						// parsedCommand[5] = company.id
						company = CompanyService.getINSTANCE().create(new Company(parsedCommand[5]));
						computer = ComputerService.getINSTANCE().create(new Computer(parsedCommand[2],
														Date.valueOf(parsedCommand[3]),
														parsedCommand[4].equals("null")? null : Date.valueOf(parsedCommand[4]),
														company));
						if (computer.getId()!=0) {
							System.out.println("computer created :"+ computer.toString());
						}
					}
						
				
					else
						System.out.println("Commande non reconnue");
					
					break;
					
					
					
				case "get":						
					if (parsedCommand[1].equals("computer")){
						// parsedCommand[2] = computer.id
						computer = ComputerService.getINSTANCE().get(Integer.parseInt(parsedCommand[2])).orElse(new Computer());
						if (computer.getId()!=0) {
							System.out.println("computer read :"+ computer.toString());
						}
					}	
					else
						System.out.println("Commande non reconnue");
					break;
				
				
					
				case "delete":
					if (parsedCommand[1].equals("computer")){
						// parsedCommand[2] = computer.id
						if (ComputerService.getINSTANCE().delete(Integer.parseInt(parsedCommand[2]))) {
							System.out.println("computer deleted");
						}
					}	
					else
						System.out.println("Commande non reconnue");
					break;
					
					
					
				case "update":
					if (parsedCommand[1].equals("computer")) {
						// parsedCommand[2] = computer.name
						// parsedCommand[3] = computer.introduced
						// parsedCommand[4] = computer.discontinued
						// parsedCommand[5] = company.id
						
						company = CompanyService.getINSTANCE().get(Integer.parseInt(parsedCommand[5])).orElse(new Company());
						if (company.getId()==0) {
							//TODO logger
							System.out.println("company does not exist");
							break;
						}
						computer = ComputerService.getINSTANCE().update(new Computer(parsedCommand[2],
														Date.valueOf(parsedCommand[3]),
														parsedCommand[4].equals("null")? null : Date.valueOf(parsedCommand[4]),
														company));
						if (computer.getId()!=0) {
							System.out.println("computer updated :"+ computer.toString());
						}
					}
						
				
					else
						System.out.println("Commande non reconnue");
					
					break;
					
					
					
				case "getall":
					switch (parsedCommand[1]) {
						case "computer":
							List<Computer> computers = ComputerService.getINSTANCE().getAll();
							for (Computer c : computers) {
								System.out.println(c);
							}
							break;
							
							
							
						case "company":
							List<Company> companies = CompanyService.getINSTANCE().getAll();
							for (Company c : companies) {
								System.out.println(c);
							}
							break;
							
							
							
						default :
							System.out.println("Commande non reconnue");
							break;
							
					}
					break;
					
					
					
				default:
					System.out.println("Commande non reconnue");
					break;
					
					
					
			}
			
			parsedCommand = scan.nextLine().split("\\s");
		}
		scan.close();
	}
}
