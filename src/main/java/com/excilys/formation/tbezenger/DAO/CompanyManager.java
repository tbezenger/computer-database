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
    private final String DELETE_COMPANY_BY_ID = "DELETE FROM company WHERE id=?";

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

	public boolean remove(int id) throws DatabaseException {
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = connectionManager.openConnection();
			conn.setAutoCommit(false);
			ComputerManager.getInstance().removeByCompanyId(id, conn);
			stmt = conn.prepareStatement(DELETE_COMPANY_BY_ID);
			stmt.setInt(1, id);
			stmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				LOGGER.error(e.toString());
				throw (new GetException());
			}
			LOGGER.error(e.toString());
			throw (new GetException());
		} finally {
			try {
				if (stmt != null && conn != null) {
					stmt.close();
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(e.toString());
				throw (new GetException());
			}
		}
		return true;
	}
}