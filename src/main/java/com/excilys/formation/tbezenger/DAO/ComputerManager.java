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
import com.excilys.formation.tbezenger.Model.Computer;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.exceptions.DAO.DeleteException;
import com.excilys.formation.tbezenger.exceptions.DAO.GetException;
import com.excilys.formation.tbezenger.exceptions.DAO.PersistException;
import com.excilys.formation.tbezenger.exceptions.DAO.UpdateException;

public class ComputerManager implements EntityManager<Computer> {

	private static ComputerManager instance;

	private final String FIND_BY_ID_QUERY = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
			+ "company.id,company.name FROM computer "
			+ "LEFT JOIN company ON company_id=company.id WHERE computer.id=?";

	private final String FIND_ALL_QUERY = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
			+ "company.id,company.name FROM computer " + "LEFT JOIN company ON company_id=company.id";

	private final String FIND_PAGE_QUERY = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
			+ "company.id,company.name FROM computer " + "LEFT JOIN company ON company_id=company.id " + "LIMIT ?,?";

	private final String PERSIST_QUERY = "INSERT INTO computer (name,introduced,discontinued,company_id) values"
			+ "(?,?,?,?);";

	private final String GET_LAST_ID_QUERY = "SELECT max(id) FROM computer";

	private final String DELETE_QUERY = "DELETE FROM computer WHERE id=?";

	private final String UPDATE_QUERY = "UPDATE computer SET name=?,introduced=?,discontinued=?,company_id=? "
			+ "WHERE id=?";

	private final String GET_PAGES_NUMBER = "SELECT count(*) FROM computer";

	private ComputerManager() {
	}

	public static ComputerManager getInstance() {
		if (instance == null) {
			instance = new ComputerManager();
		}
		return instance;
	}

	@Override
	public Optional<Computer> findById(int id) throws DatabaseException {
		Computer computer = null;
		try (Connection conn = openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_QUERY);
			stmt.setInt(1, id);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			if (rs.next()) {
				Company company = new Company(rs.getInt("company.id"), rs.getString("company.name"));
				computer = new Computer(rs.getInt("id"), rs.getString("name"), rs.getDate("introduced"),
						rs.getDate("discontinued"), company);
			}
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return Optional.ofNullable(computer);
	}

	@Override
	public List<Computer> findall() throws DatabaseException {
		List<Computer> computers = new ArrayList<Computer>();
		try (Connection conn = openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(FIND_ALL_QUERY);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				Company company = new Company(rs.getInt("company.id"), rs.getString("company.name"));
				computers.add(new Computer(rs.getInt("id"), rs.getString("name"), rs.getDate("introduced"),
						rs.getDate("discontinued"), company));
			}
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return computers;
	}

	public List<Computer> findPage(int numpage, int rowsByPage) throws DatabaseException {
		List<Computer> computers = new ArrayList<Computer>();
		try (Connection conn = openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(FIND_PAGE_QUERY);

			stmt.setInt(1, numpage * rowsByPage);
			stmt.setInt(2, rowsByPage);

			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				Company company = new Company(rs.getInt("company.id"), rs.getString("company.name"));
				computers.add(new Computer(rs.getInt("id"), rs.getString("name"), rs.getDate("introduced"),
						rs.getDate("discontinued"), company));
			}
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return computers;
	}

	public Computer persist(Computer t) throws DatabaseException {
		try (Connection conn = openConnection()) {
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(PERSIST_QUERY);

			stmt.setString(1, t.getName());
			stmt.setDate(2, t.getIntroduced());
			stmt.setDate(3, t.getDiscontinued());
			stmt.setInt(4, t.getCompany().getId());

			stmt.executeUpdate();
			stmt.executeQuery(GET_LAST_ID_QUERY);
			stmt.getResultSet().next();
			t.setId(stmt.getResultSet().getInt("max(id)"));
			conn.commit();
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new PersistException());
		}
		return t;
	}

	public boolean remove(int id) throws DatabaseException {
		try (Connection conn = openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new DeleteException());
		}
		return true;
	}

	public boolean update(Computer t) throws DatabaseException {
		try (Connection conn = openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY);

			stmt.setString(1, t.getName());
			stmt.setDate(2, t.getIntroduced());
			stmt.setDate(3, t.getDiscontinued());
			stmt.setInt(4, t.getCompany().getId());
			stmt.setInt(5, t.getId());
			stmt.executeUpdate();

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error(e.toString());
			throw (new UpdateException());
		}
		return true;
	}

	public int getComputersNumber() throws DatabaseException {
		int computersNumber = 0;
		try (Connection conn = openConnection()) {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(GET_PAGES_NUMBER);
			stmt.getResultSet().next();
			computersNumber = stmt.getResultSet().getInt("count(*)");
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return computersNumber;
	}

}