package com.excilys.formation.tbezenger;

import java.sql.Date;
import com.excilys.formation.tbezenger.DAO.CompanyManager;
import com.excilys.formation.tbezenger.DAO.ComputerManager;
import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;

public class App {
	public static void main(String[] args) {
		try {
			System.out.println(ComputerManager.getINSTANCE().findById(123).orElse(new Computer()));
			Company c = CompanyManager.getINSTANCE().findById(5).orElse(new Company());
			System.out.println(c);
			Computer pc = ComputerManager.getINSTANCE().persist(new Computer("pc",Date.valueOf("2000-10-10"),Date.valueOf("2000-10-15"),c));
			System.out.println(pc);
			ComputerManager.getINSTANCE().remove(pc.getId());
			System.out.println(CompanyManager.getINSTANCE().findPage(2));

		} catch(Exception e){e.printStackTrace();}
	}
}
