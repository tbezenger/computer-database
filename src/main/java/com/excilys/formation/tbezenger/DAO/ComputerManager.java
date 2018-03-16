package com.excilys.formation.tbezenger.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.exceptions.DAO.DeleteException;
import com.excilys.formation.tbezenger.exceptions.DAO.GetException;
import com.excilys.formation.tbezenger.exceptions.DAO.PersistException;
import com.excilys.formation.tbezenger.exceptions.DAO.UpdateException;
import com.excilys.formation.tbezenger.model.Company;
import com.excilys.formation.tbezenger.model.Computer;
import com.excilys.formation.tbezenger.model.ComputerPage;

import static com.excilys.formation.tbezenger.Strings.COMPANY_ID;
import static com.excilys.formation.tbezenger.Strings.COMPANY_NAME;
import static com.excilys.formation.tbezenger.Strings.COMPUTER_ID;
import static com.excilys.formation.tbezenger.Strings.COMPUTER_NAME;
import static com.excilys.formation.tbezenger.Strings.COMPUTER_DISCONTINUED;
import static com.excilys.formation.tbezenger.Strings.COMPUTER_INTRODUCED;


@Repository
public class ComputerManager implements EntityManager<Computer> {


    private JdbcTemplate jdbcTemplate;
    @Autowired
	public ComputerManager(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final String FIND_BY_ID_QUERY = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
			+ "company.id,company.name FROM computer "
			+ "LEFT JOIN company ON company_id=company.id WHERE computer.id=?";

	private final String FIND_ALL_QUERY = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
			+ "company.id,company.name FROM computer " + "LEFT JOIN company ON company_id=company.id";

	private final String PERSIST_QUERY = "INSERT INTO computer (name,introduced,discontinued,company_id) values"
			+ "(?,?,?,?);";

	private final String DELETE_QUERY = "DELETE FROM computer WHERE id=?";

	private final String DELETE_QUERY_BY_COMPANY_ID = "DELETE FROM computer WHERE company_id=?";

	private final String UPDATE_QUERY = "UPDATE computer SET name=?,introduced=?,discontinued=?,company_id=? "
			+ "WHERE id=?";


	private final String GET_COMPUTERS_COUNT = "SELECT count(*) FROM computer LEFT JOIN company ON company_id=company.id"
			+ " WHERE computer.name LIKE ? OR "
			+ "company.name LIKE ?";

	private final String GET_COMPUTERS = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
			+ "company.id,company.name FROM computer LEFT JOIN company ON company_id=company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY computer.name ASC LIMIT ?,?";


	@Override
	public Optional<Computer> findById(int id) throws DatabaseException {
		Computer computer;
		try {
			computer = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,
					new Object[]{id},
					new RowMapper<Computer>() {
				public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
					Company company = new Company(rs.getInt(COMPANY_ID), rs.getString(COMPANY_NAME));
					Computer computer = new Computer(rs.getInt(COMPUTER_ID), rs.getString(COMPUTER_NAME), rs.getDate(COMPUTER_INTRODUCED),
							rs.getDate(COMPUTER_DISCONTINUED), company);
					return computer;
				}
			});
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return Optional.ofNullable(computer);
	}

	@Override
	public List<Computer> findall() throws DatabaseException {
		List<Computer> computers = new ArrayList<Computer>();
		try {
	        computers = jdbcTemplate.query(FIND_ALL_QUERY, new RowMapper<Computer>() {
							public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
								Company company = new Company(rs.getInt(COMPANY_ID), rs.getString(COMPANY_NAME));
								Computer computer = new Computer(rs.getInt(COMPUTER_ID), rs.getString(COMPUTER_NAME), rs.getDate(COMPUTER_INTRODUCED),
										rs.getDate(COMPUTER_DISCONTINUED), company);
								return computer;
							}
						});
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return computers;
	}

	@Transactional
	public ComputerPage findPage(int numpage, int rowsByPage, String search, String orderBy, String order, boolean isAscending) throws DatabaseException {
		ComputerPage page = new ComputerPage();
		try {
			page.setComputers(jdbcTemplate.query(GET_COMPUTERS, new Object[]{"%" + search + "%", "%" + search + "%"/*, orderBy, order*/, (numpage - 1) * rowsByPage, rowsByPage},
												 new RowMapper<Computer>() {
				public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
					Company company = new Company(rs.getInt(COMPANY_ID), rs.getString(COMPANY_NAME));
					Computer computer = new Computer(rs.getInt(COMPUTER_ID), rs.getString(COMPUTER_NAME), rs.getDate(COMPUTER_INTRODUCED),
							rs.getDate(COMPUTER_DISCONTINUED), company);
					return computer;
				}
			}));
			page.setTotalResults(jdbcTemplate.queryForObject(GET_COMPUTERS_COUNT, new Object[]{"%" + search + "%", "%" + search + "%"}, Integer.class));
			page.setRows(rowsByPage);
			page.setMaxPage(page.getTotalResults() / page.getRows() + 1);
			page.setNumPage(numpage);
			page.setSearch(search);
			page.setOrderBy(orderBy);
			page.setAscending(isAscending);
		} catch (DataAccessException e) {
			e.printStackTrace();
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return page;
	}

	public boolean persist(Computer computer) throws DatabaseException {

		try {
	        jdbcTemplate.update(PERSIST_QUERY, computer.getName(), computer.getIntroduced(),
	        								   computer.getDiscontinued(), computer.getCompany().getId()
	                );
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new PersistException());
		}
		return true;
	}

	public boolean remove(int id) throws DatabaseException {
		try {
			jdbcTemplate.update(DELETE_QUERY, id);
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new DeleteException());
		}
		return true;
	}

	public boolean removeByCompanyId(int companyId) throws DatabaseException {
		try {
			jdbcTemplate.update(DELETE_QUERY_BY_COMPANY_ID, companyId);
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new DeleteException());
		}
		return true;
	}

	public boolean update(Computer computer) throws DatabaseException {
		try {
			jdbcTemplate.update(UPDATE_QUERY, computer.getName(), computer.getIntroduced(),
								computer.getIntroduced(), computer.getDiscontinued(),
								computer);
		} catch (DataAccessException e) {
			e.printStackTrace();
			LOGGER.error(e.toString());
			throw (new UpdateException());
		}
		return true;
	}
}