package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.Model.Company;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.exceptions.DAO.GetException;

public class CompanyManager implements EntityManager<Company> {

    private static CompanyManager instance;
    private final String FIND_ALL_QUERY = "SELECT id,name FROM company";
    private final String FIND_BY_ID_QUERY = "SELECT id,name FROM company WHERE id=?";

    private CompanyManager() { }

    public static CompanyManager getInstance() {
        if (instance == null) {
           instance = new CompanyManager();
        }
        return instance;
    }

    ConnectionManager connectionManager = ConnectionManager.getInstance();

    @Override
    public List<Company> findall() throws DatabaseException {
        List<Company> companies = new ArrayList<Company>();
        try (Connection conn = connectionManager.openConnection()) {
            Statement stmt = conn.createStatement();
			stmt.executeQuery(FIND_ALL_QUERY);
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				companies.add(new Company(rs.getInt("id"), rs.getString("name")));
			}
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return companies;
	}

	@Override
	public Optional<Company> findById(int id) throws DatabaseException {
		Company company = null;
		try (Connection conn = connectionManager.openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_QUERY);
			stmt.setInt(1, id);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			rs.next();
			company = new Company(rs.getInt("id"), rs.getString("name"));
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return Optional.ofNullable(company);
	}

}