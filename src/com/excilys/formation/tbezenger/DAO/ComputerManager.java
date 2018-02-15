package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.Model.Computer;

public class ComputerManager implements EntityManager<Computer>{
	private static ComputerManager INSTANCE;
	
	private final String FIND_BY_ID_QUERY = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
												 + "company.id,company.name FROM computer "
												 + "LEFT JOIN company ON company_id=company.id WHERE computer.id=?";
	
	private final String FIND_ALL_QUERY = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
											   + "company.id,company.name FROM computer "
											   + "LEFT JOIN company ON company_id=company.id";
	
	private final String FIND_PAGE_QUERY = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
												+ "company.id,company.name FROM computer "
												+ "LEFT JOIN company ON company_id=company.id "
												+ "LIMIT ?,?";
	
	private final String PERSIST_QUERY = "INSERT INTO computer (name,introduced,discontinued,company_id) values"
													+"(\"?\",'?','?',?);";
	
	private final String GET_LAST_ID_QUERY = "SELECT max(id) FROM computer";
	
	private final String DELETE_QUERY = "DELETE FROM computer WHERE id=?";
	
	private final String UPDATE_QUERY = "UPDATE computer SET name=\"?,introduced='?',discontinued='?',company_id=?"
											  +"WHERE id=?";
	
	private ComputerManager(){}
	
	public static ComputerManager getINSTANCE() {
		if (INSTANCE==null) {
			INSTANCE = new ComputerManager();
		}
		return INSTANCE;
	}
	
	private Connection conn;


	public Optional<Computer> findById(int id) throws Exception {
		Computer computer=null;
		this.conn = openConnection();
		PreparedStatement stmt = this.conn.prepareStatement(FIND_BY_ID_QUERY);
		stmt.setInt(1, id);
		stmt.executeQuery();
		ResultSet rs = stmt.getResultSet();
		if (rs.next()) {
			Company company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			computer = new Computer(rs.getInt("id"),rs.getString("name"),
									  rs.getDate("introduced"),rs.getDate("discontinued"),company);
		}
		stmt.close();
		this.conn.close();
		return Optional.ofNullable(computer);
	}

	@Override
	public List<Computer> findall() throws Exception {
		List<Computer> computers = new ArrayList<Computer>();
		this.conn = openConnection();
		PreparedStatement stmt = this.conn.prepareStatement(FIND_ALL_QUERY);
		stmt.executeQuery();
		ResultSet rs = stmt.getResultSet();
		while (rs.next()) {
			Company company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			computers.add(new Computer(rs.getInt("id"),rs.getString("name"),
									   rs.getDate("introduced"),rs.getDate("discontinued"),company));
		}
		stmt.close();
		this.conn.close();
		return computers;
	}

	public List<Computer> findPage(int numpage, int rowsByPage) throws Exception{
		List<Computer> computers = new ArrayList<Computer>();
		this.conn = openConnection();
		PreparedStatement stmt = this.conn.prepareStatement(FIND_PAGE_QUERY);
		
		stmt.setInt(1,numpage*rowsByPage);
		stmt.setInt(1,rowsByPage);
		
		stmt.executeQuery();
		ResultSet rs = stmt.getResultSet();
		while (rs.next()) {
			Company company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			computers.add(new Computer(rs.getInt("id"),rs.getString("name"),rs.getDate("introduced"),
									   rs.getDate("discontinued"),company));
		}
		stmt.close();
		this.conn.close();
		return computers;
	}
	

	public Computer persist(Computer t) throws Exception {
		this.conn = openConnection();
		this.conn.setAutoCommit(false);
		PreparedStatement stmt = this.conn.prepareStatement(PERSIST_QUERY);
		
		stmt.setString(1, t.getName());
		stmt.setDate(2, t.getIntroduced());
		stmt.setDate(3, t.getDiscontinued());
		stmt.setInt(4,t.getCompany().getId());
		
		stmt.executeUpdate();
		stmt.executeQuery(GET_LAST_ID_QUERY);
		stmt.getResultSet().next();
		t.setId(stmt.getResultSet().getInt("max(id)"));
		this.conn.commit();
		stmt.close();
		this.conn.close();
		return t;
	}


	public boolean remove(int id) throws Exception {
		this.conn = openConnection();
		PreparedStatement stmt = this.conn.prepareStatement(DELETE_QUERY);
		stmt.executeUpdate();
		this.conn.close();
		return true;
	}


	public boolean update(Computer t) throws Exception {
		this.conn = openConnection();
		PreparedStatement stmt = this.conn.prepareStatement(UPDATE_QUERY);
		
		stmt.setString(1, t.getName());
		stmt.setDate(2, t.getIntroduced());
		stmt.setDate(3, t.getDiscontinued());
		stmt.setInt(4, t.getCompany().getId());
		stmt.setInt(5, t.getId());
		
		this.conn.close();
		return true;
	}

}
