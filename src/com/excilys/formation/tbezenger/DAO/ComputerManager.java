package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;

public class ComputerManager implements EntityManager<Computer>{
	private static ComputerManager INSTANCE = new ComputerManager();
	
	private ComputerManager(){}
	
	public static ComputerManager getINSTANCE() {
		return INSTANCE;
	}
	
	private Connection conn;

	@Override
	public Optional<Computer> findById(int id) throws Exception {
		Computer computer=null;
		this.conn = openConnection();
		Statement stmt = this.conn.createStatement();
		stmt.executeQuery("SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
						+ "company.id,company.name FROM computer "
						+ "LEFT JOIN company ON company_id=company.id WHERE computer.id="+id);
		
		ResultSet rs = stmt.getResultSet();
		if (rs.next()) {
			Company company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			computer = new Computer(rs.getInt("id"),rs.getString("name"),
									  rs.getDate("introduced"),rs.getDate("discontinued"),company);
		}
		this.conn.close();
		return Optional.ofNullable(computer);
	}

	@Override
	public List<Computer> findall() throws Exception {
		List<Computer> computers = new ArrayList<Computer>();
		this.conn = openConnection();
		Statement stmt = this.conn.createStatement();
		stmt.executeQuery("SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
				+ "company.id,company.name FROM computer "
				+ "LEFT JOIN company ON company_id=company.id");
		ResultSet rs = stmt.getResultSet();
		while (rs.next()) {
			Company company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			computers.add(new Computer(rs.getInt("id"),rs.getString("name"),rs.getDate("introduced"),rs.getDate("discontinued"),company));
		}
		this.conn.close();
		return computers;
	}

	public List<Computer> findPage(int numpage) throws Exception{
		List<Computer> computers = new ArrayList<Computer>();
		this.conn = openConnection();
		Statement stmt = this.conn.createStatement();
		stmt.executeQuery("SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
				+ "company.id,company.name FROM computer "
				+ "LEFT JOIN company ON company_id=company.id "
				+ "LIMIT "+20*numpage+",20");
		ResultSet rs = stmt.getResultSet();
		while (rs.next()) {
			Company company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			computers.add(new Computer(rs.getInt("id"),rs.getString("name"),rs.getDate("introduced"),rs.getDate("discontinued"),company));
		}
		this.conn.close();
		return computers;
	}
	
	@Override
	public Computer persist(Computer t) throws Exception {
		this.conn = openConnection();
		this.conn.setAutoCommit(false);
		Statement stmt = this.conn.createStatement();
		stmt.executeUpdate("INSERT INTO computer (name,introduced,discontinued,company_id) values (\""
							+t.getName()+"\",'"+t.getIntroduced()+"','"+t.getDiscontinued()+"',"+t.getCompany().getId()+");");
		stmt.executeQuery("SELECT max(id) FROM computer");
		stmt.getResultSet().next();
		t.setId(stmt.getResultSet().getInt("max(id)"));
		this.conn.commit();
		this.conn.close();
		return t;
	}

	@Override
	public boolean remove(int id) throws Exception {
		this.conn = openConnection();
		Statement stmt = this.conn.createStatement();
		stmt.executeUpdate("DELETE FROM computer WHERE id="+id);
		this.conn.close();
		return true;
	}

	@Override
	public boolean update(Computer t) throws Exception {
		this.conn = openConnection();
		Statement stmt = this.conn.createStatement();
		stmt.executeUpdate("UPDATE computer SET name=\""+t.getName()+",introduced='"+t.getIntroduced()+
												"',discontinued='"+t.getDiscontinued()+"',"+t.getCompany().getId()+
										    "WHERE id="+t.getId());
		this.conn.close();
		return true;
	}

}
