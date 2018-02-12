package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.excilys.formation.tbezenger.Utils;
import com.excilys.formation.tbezenger.Model.Company;

public class CompanyManager implements EntityManager<Company>{
	private static CompanyManager INSTANCE = new CompanyManager();
	private Connection conn;
	
	private CompanyManager() {
		try {
			this.conn =  DriverManager.getConnection(Utils.url, Utils.dbName, Utils.dbPassword);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static CompanyManager getINSTANCE() {
		return INSTANCE;
	}

	@Override
	public Company find(int id) throws Exception {
		Statement stmt = this.conn.createStatement();
		stmt.executeQuery("SELECT * FROM company WHERE id="+Integer.toString(id));
		ResultSet rs = stmt.getResultSet();
		rs.next();
		return new Company(rs.getInt("id"),rs.getString("name"));
	}

	@Override
	public List<Company> findall() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean persist(Company t) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(int id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Company t) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
