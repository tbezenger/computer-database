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
import com.excilys.formation.tbezenger.Model.ComputerPage;
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

	private final String DELETE_QUERY_BY_COMPANY_ID = "DELETE FROM computer WHERE company_id=?";

	private final String UPDATE_QUERY = "UPDATE computer SET name=?,introduced=?,discontinued=?,company_id=? "
			+ "WHERE id=?";

	private final String GET_COMPUTERS_NUMBER = "SELECT count(*) FROM computer";

	private final String GET_COMPUTERS_BY_SEARCH = "SELECT * FROM computer LEFT JOIN company ON company_id=company.id "
												 + "WHERE computer.name LIKE ? OR company.name LIKE ?  LIMIT ?,?";

	private final String GET_TOTAL_COMPUTERS_BY_SEARCH = "SELECT count(*) FROM computer LEFT JOIN company ON company_id=company.id "
													   + "WHERE computer.name LIKE ? OR company.name LIKE ?";

//	private final String GET_ORDERED_BY_COMPUTER_NAME_PAGE = "SELECT * FROM computer LEFT JOIN company ON company_id=company.id"
//														  + " ORDER BY computer.name LIMIT ?,?";

	private final String GET_ORDERED_BY_COMPUTER_NAME_SEARCH_PAGE = "SELECT * FROM computer LEFT JOIN company ON company_id=company.id "
																  + "WHERE computer.name LIKE ? OR company.name LIKE ?"
																  + "ORDER BY computer.name LIMIT ?,?";

	private final String GET_ORDERED_BY_COMPANY_NAME_SEARCH_PAGE = "SELECT * FROM computer LEFT JOIN company ON company_id=company.id "
																 + "WHERE computer.name LIKE ? OR company.name LIKE ?"
																 + "ORDER BY company.name LIMIT ?,?";

	private ComputerManager() {
	}

	public static ComputerManager getInstance() {
		if (instance == null) {
			instance = new ComputerManager();
		}
		return instance;
	}

    ConnectionManager connectionManager = ConnectionManager.getInstance();

	@Override
	public Optional<Computer> findById(int id) throws DatabaseException {
		Computer computer = null;
		try (Connection conn = connectionManager.openConnection()) {
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
		try (Connection conn = connectionManager.openConnection()) {
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

	public ComputerPage findPage(int numpage, int rowsByPage) throws DatabaseException {
		ComputerPage page = new ComputerPage();
		try (Connection conn = connectionManager.openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(FIND_PAGE_QUERY);

			stmt.setInt(1, (numpage - 1) * rowsByPage);
			stmt.setInt(2, rowsByPage);

			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				Company company = new Company(rs.getInt("company.id"), rs.getString("company.name"));
				page.getComputers().add(new Computer(rs.getInt("id"), rs.getString("name"), rs.getDate("introduced"),
						rs.getDate("discontinued"), company));
			}
			stmt.close();
			page.setTotalResults(getComputersNumber());
			page.setRows(rowsByPage);
			page.setMaxPage(page.getTotalResults() / rowsByPage + 1);
			page.setNumPage(numpage);
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return page;
	}

	public Computer persist(Computer t) throws DatabaseException {
		try (Connection conn = connectionManager.openConnection()) {
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
		try (Connection conn = connectionManager.openConnection()) {
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

	public boolean removeByCompanyId(int companyId) throws DatabaseException {
		try (Connection conn = connectionManager.openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY_BY_COMPANY_ID);
			stmt.setInt(1, companyId);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new DeleteException());
		}
		return true;
	}

	public boolean update(Computer t) throws DatabaseException {
		try (Connection conn = connectionManager.openConnection()) {
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
		try (Connection conn = connectionManager.openConnection()) {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(GET_COMPUTERS_NUMBER);
			stmt.getResultSet().next();
			computersNumber = stmt.getResultSet().getInt("count(*)");
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return computersNumber;
	}

	public ComputerPage getComputersPageBySearch(String search, int rowsByPage, int numpage) throws DatabaseException {
		ComputerPage page = new ComputerPage();
		page.setRows(rowsByPage);
		try (Connection conn = connectionManager.openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(GET_COMPUTERS_BY_SEARCH);
			stmt.setString(1, "%" + search + "%");
			stmt.setString(2, "%" + search + "%");
			stmt.setInt(3, rowsByPage * (numpage - 1));
			stmt.setInt(4, rowsByPage);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				Company company = new Company(rs.getInt("company.id"), rs.getString("company.name"));
				page.getComputers().add(new Computer(rs.getInt("id"), rs.getString("name"), rs.getDate("introduced"),
						rs.getDate("discontinued"), company));
			}
			stmt.close();
			page.setTotalResults(getTotalRelevantRows(search));
			page.setMaxPage(page.getTotalResults() / rowsByPage + 1);
			page.setNumPage(numpage);
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return page;
	}

	public int getTotalRelevantRows(String search) throws DatabaseException {
		int total = 0;
		PreparedStatement stmt = null;
		try (Connection conn = connectionManager.openConnection()) {
			stmt = conn.prepareStatement(GET_TOTAL_COMPUTERS_BY_SEARCH);
			stmt.setString(1, "%" + search + "%");
			stmt.setString(2, "%" + search + "%");
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			rs.next();
			total = rs.getInt("count(*)");
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.error(e.toString());
					throw (new GetException());
				}
			}
		}
		return total;
	}

	public ComputerPage getOrderedPage(ComputerPage page, String orderBy, String search) throws DatabaseException {
		PreparedStatement stmt = null;
		page.getComputers().clear();
		try (Connection conn = connectionManager.openConnection()) {
			switch (orderBy) {
			case "computer.name" :
				stmt = conn.prepareStatement(GET_ORDERED_BY_COMPUTER_NAME_SEARCH_PAGE);
				break;
			case "company.name" :
				stmt = conn.prepareStatement(GET_ORDERED_BY_COMPANY_NAME_SEARCH_PAGE);
				break;
			default :
				stmt = conn.prepareStatement(GET_ORDERED_BY_COMPUTER_NAME_SEARCH_PAGE);
				break;
			}
			stmt.setString(1, "%" + search + "%");
			stmt.setString(2, "%" + search + "%");
			stmt.setInt(3, (page.getNumPage() - 1) * page.getRows());
			stmt.setInt(4, page.getRows());
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				Company company = new Company(rs.getInt("company.id"), rs.getString("company.name"));
				page.getComputers().add(new Computer(rs.getInt("id"), rs.getString("name"), rs.getDate("introduced"),
						rs.getDate("discontinued"), company));
			}
			page.setTotalResults(getTotalRelevantRows(search));
			page.setMaxPage(page.getTotalResults() / page.getRows() + 1);
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.error(e.toString());
					throw (new GetException());
				}
			}
		}
		return page;
	}

}