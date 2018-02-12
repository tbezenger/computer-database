package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.excilys.formation.tbezenger.Utils;
import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;

public class ComputerManager implements EntityManager<Computer>{
	private static ComputerManager INSTANCE = new ComputerManager();
	
	private ComputerManager(){
		try {
			this.conn =  DriverManager.getConnection(Utils.url, Utils.dbName, Utils.dbPassword);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ComputerManager getINSTANCE() {
		return INSTANCE;
	}
	
	private Connection conn;

	@Override
	public Computer find(int id) throws Exception {
		Statement stmt = this.conn.createStatement();
		stmt.executeQuery("SELECT * FROM computer,company WHERE computer.id="+Integer.toString(id)+" AND computer.company_id=company.id");
		ResultSet rs = stmt.getResultSet();
		rs.next();
		Company company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
		Computer computer = new Computer(rs.getInt("id"),rs.getString("name"),
								  rs.getDate("introduced"),rs.getDate("discontinued"),company);
		return computer;
	}

	@Override
	public List<Computer> findall() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean persist(Computer t) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(int id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Computer t) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
