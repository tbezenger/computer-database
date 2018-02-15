package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.Model.Company;

public class CompanyManager implements EntityManager<Company>{
	private static CompanyManager INSTANCE;
	private Connection conn;
	private final String FIND_ALL_QUERY = "SELECT id,name FROM company";
	
	private CompanyManager() {}
	
	public static CompanyManager getINSTANCE() {
		if (INSTANCE==null) {
			INSTANCE = new CompanyManager();
		}
		return INSTANCE;
	}

	@Override
	public List<Company> findall() throws Exception {
		List<Company> companies = new ArrayList<Company>();
		this.conn = openConnection();
		Statement stmt = this.conn.createStatement();
		stmt.executeQuery(FIND_ALL_QUERY);
		ResultSet rs = stmt.getResultSet();
		while(rs.next()) {
			companies.add(new Company(rs.getInt("id"),rs.getString("name")));
		}
		return companies;
	}

}
