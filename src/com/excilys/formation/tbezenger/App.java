package com.excilys.formation.tbezenger;

import java.sql.Date;
import com.excilys.formation.tbezenger.DAO.CompanyManager;
import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;

public class App {
	public static void main(String[] args) {
		try {
			System.out.println(ComputerManager.getINSTANCE().find(5));
			Company c = CompanyManager.getINSTANCE().find(2);
			System.out.println(c);
			Computer pc = ComputerManager.getINSTANCE().persist(new Computer("pc",Date.valueOf("2000-10-10"),Date.valueOf("2000-10-15"),c));
			System.out.println(pc);
			ComputerManager.getINSTANCE().remove(pc.getId());

		} catch(Exception e){e.printStackTrace();}
	}
}
