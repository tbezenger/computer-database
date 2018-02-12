package com.excilys.formation.tbezenger;

import com.excilys.formation.tbezenger.DAO.CompanyManager;
import com.excilys.formation.tbezenger.DAO.ComputerManager;

public class App {
	public static void main(String[] args) {
		try {
			System.out.println(ComputerManager.getINSTANCE().find(1));
			System.out.println(CompanyManager.getINSTANCE().find(2));

		} catch(Exception e){e.printStackTrace();}
	}
}
