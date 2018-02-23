package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.DTO.CompanyDTO;
import com.excilys.formation.tbezenger.DTO.ComputerDTO;
import com.excilys.formation.tbezenger.Exceptions.DatabaseException;
import com.excilys.formation.tbezenger.Model.Computer;

public class ComputerManager implements EntityManager<ComputerDTO> {

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

	private final String UPDATE_QUERY = "UPDATE computer SET name=?,introduced=?,discontinued=?,company_id=?"
			+ "WHERE id=?";

	private final String GET_PAGES_NUMBER = "SELECT count(*) FROM computers";

	private ComputerManager() {
	}

	public static ComputerManager getInstance() {
		if (instance == null) {
			instance = new ComputerManager();
		}
		return instance;
	}


	public ComputerDTO createDTOFromBean(Computer computer) {
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setId(computer.getId());
		computerDTO.setName(computer.getName());
		computerDTO.setIntroduced(computer.getIntroduced());
		computerDTO.setDiscontinued(computer.getDiscontinued());
		computerDTO.setCompany(CompanyManager.getInstance().createDTOFromBean(computer.getCompany()));
		return computerDTO;
	}

	public Computer createBeanFromDTO(ComputerDTO computerDTO) {
		return new Computer(computerDTO.getId(),computerDTO.getName(),computerDTO.getIntroduced(),
							computerDTO.getDiscontinued(),
							CompanyManager.getInstance().createBeanFromDTO(computerDTO.getCompany()));
	}


	@Override
	public Optional<ComputerDTO> findById(int id) throws DatabaseException {
		ComputerDTO computerDTO = null;
		try (Connection conn = openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_QUERY);
			stmt.setInt(1, id);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			if (rs.next()) {
				CompanyDTO companyDTO = new CompanyDTO();
				companyDTO.setId(rs.getInt("company.id"));
				companyDTO.setName(rs.getString("company.name"));
				computerDTO = new ComputerDTO();
				computerDTO.setId(rs.getInt("id"));
				computerDTO.setName(rs.getString("name"));
				computerDTO.setIntroduced(rs.getDate("introduced"));
				computerDTO.setDiscontinued(rs.getDate("discontinued"));
				computerDTO.setCompany(companyDTO);
			}
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new DatabaseException(DatabaseException.GET_FAIL));
		}
		return Optional.ofNullable(computerDTO);
	}

	@Override
	public List<ComputerDTO> findall() throws DatabaseException {
		List<ComputerDTO> computersDTO = new ArrayList<ComputerDTO>();
		try (Connection conn = openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(FIND_ALL_QUERY);
			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				CompanyDTO companyDTO = new CompanyDTO();
				companyDTO.setId(rs.getInt("company.id"));
				companyDTO.setName(rs.getString("company.name"));
				ComputerDTO computerDTO = new ComputerDTO();
				computerDTO.setId(rs.getInt("id"));
				computerDTO.setName(rs.getString("name"));
				computerDTO.setIntroduced(rs.getDate("introduced"));
				computerDTO.setDiscontinued(rs.getDate("discontinued"));
				computerDTO.setCompany(companyDTO);
				computersDTO.add(computerDTO);
			}
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new DatabaseException(DatabaseException.GET_FAIL));
		}
		return computersDTO;
	}

	public List<ComputerDTO> findPage(int numpage, int rowsByPage) throws DatabaseException {
		List<ComputerDTO> computersDTO = new ArrayList<ComputerDTO>();
		try (Connection conn = openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(FIND_PAGE_QUERY);

			stmt.setInt(1, numpage * rowsByPage);
			stmt.setInt(2, rowsByPage);

			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				CompanyDTO companyDTO = new CompanyDTO();
				companyDTO.setId(rs.getInt("company.id"));
				companyDTO.setName(rs.getString("company.name"));
				ComputerDTO computerDTO = new ComputerDTO();
				computerDTO.setId(rs.getInt("id"));
				computerDTO.setName(rs.getString("name"));
				computerDTO.setIntroduced(rs.getDate("introduced"));
				computerDTO.setDiscontinued(rs.getDate("discontinued"));
				computerDTO.setCompany(companyDTO);
				computersDTO.add(computerDTO);
			}
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new DatabaseException(DatabaseException.GET_FAIL));
		}
		return computersDTO;
	}

	public ComputerDTO persist(ComputerDTO computerDTO) throws DatabaseException {
		try (Connection conn = openConnection()) {
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(PERSIST_QUERY);

			stmt.setString(1, computerDTO.getName());
			stmt.setDate(2, computerDTO.getIntroduced());
			stmt.setDate(3, computerDTO.getDiscontinued());
			stmt.setInt(4, computerDTO.getCompany().getId());

			stmt.executeUpdate();
			stmt.executeQuery(GET_LAST_ID_QUERY);
			stmt.getResultSet().next();
			computerDTO.setId(stmt.getResultSet().getInt("max(id)"));
			conn.commit();
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new DatabaseException(DatabaseException.PERSISTENCE_FAIL));
		}
		return computerDTO;
	}

	public boolean remove(int id) throws DatabaseException {
		try (Connection conn = openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new DatabaseException(DatabaseException.DELETE_FAIL));
		}
		return true;
	}

	public boolean update(ComputerDTO computerDTO) throws DatabaseException {
		try (Connection conn = openConnection()) {
			PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY);

			stmt.setString(1, computerDTO.getName());
			stmt.setDate(2, computerDTO.getIntroduced());
			stmt.setDate(3, computerDTO.getDiscontinued());
			stmt.setInt(4, computerDTO.getCompany().getId());
			stmt.setInt(5, computerDTO.getId());

			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new DatabaseException(DatabaseException.UPDATE_FAIL));
		}
		return true;
	}

	public int getComputersNumber() throws DatabaseException {
		int computersNumber = 0;
		try (Connection conn = openConnection()) {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(GET_PAGES_NUMBER);
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error(e.toString());
			throw (new DatabaseException(DatabaseException.GET_FAIL));
		}
		return computersNumber;
	}

}
