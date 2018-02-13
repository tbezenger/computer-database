package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.Model.Company;

public class CompanyManager implements EntityManager<Company>{
	private static CompanyManager INSTANCE = new CompanyManager();
	private Connection conn;
	
	private CompanyManager() {}
	
	public static CompanyManager getINSTANCE() {
		return INSTANCE;
	}
	
	@Override
	public Optional<Company> findById(int id) throws Exception {
		this.conn = openConnection();
		Statement stmt = this.conn.createStatement();
		stmt.executeQuery("SELECT id,name FROM company WHERE id="+id);
		ResultSet rs = stmt.getResultSet();
		rs.next();
		Company company = new Company(rs.getInt("id"),rs.getString("name"));
		this.conn.close();
		return Optional.ofNullable(company);
	}

	@Override
	public List<Company> findall() throws Exception {
		List<Company> companies = new ArrayList<Company>();
		this.conn = openConnection();
		Statement stmt = this.conn.createStatement();
		stmt.executeQuery("SELECT id,name FROM company");
		ResultSet rs = stmt.getResultSet();
		while(rs.next()) {
			companies.add(new Company(rs.getInt("id"),rs.getString("name")));
		}
		return companies;
	}
	
	@Override
	public List<Company> findPage(int numpage) throws Exception {
		List<Company> companies = new ArrayList<Company>();
		this.conn = openConnection();
		Statement stmt = this.conn.createStatement();
		stmt.executeQuery("SELECT id,name FROM company LIMIT "+20*numpage+",20");
		ResultSet rs = stmt.getResultSet();
		while(rs.next()) {
			companies.add(new Company(rs.getInt("id"),rs.getString("name")));
		}
		return companies;
	}

	@Override
	public Company persist(Company t) throws Exception {
		this.conn = openConnection();
		this.conn.setAutoCommit(false);
		Statement stmt = this.conn.createStatement();
		stmt.executeUpdate("INSERT INTO company (name) VALUES(\""+t.getName()+"\");");
		stmt.executeQuery("SELECT max(id) FROM company");
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
		stmt.executeUpdate("DELETE FROM company WHERE id="+id);
		this.conn.close();
		return true;
	}

	@Override
	public boolean update(Company t) throws Exception {
		this.conn = openConnection();
		Statement stmt = this.conn.createStatement();
		stmt.executeUpdate("UPDATE company SET name="+t.getName()+"WHERE id="+t.getId());
		this.conn.close();
		return true;
	}

}
