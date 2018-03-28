package com.excilys.formation.tbezenger.cdb.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.GetException;
import com.excilys.formation.tbezenger.cdb.exceptions.DAO.PersistException;
import com.excilys.formation.tbezenger.cdb.exceptions.DAO.UpdateException;
import com.excilys.formation.tbezenger.cdb.model.ComputerPage;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DeleteException;
import com.excilys.formation.tbezenger.cdb.model.Company;
import com.excilys.formation.tbezenger.cdb.model.Computer;

import static com.excilys.formation.tbezenger.cdb.Strings.COMPANY_ID;
import static com.excilys.formation.tbezenger.cdb.Strings.COMPANY_NAME;
import static com.excilys.formation.tbezenger.cdb.Strings.COMPUTER_ID;
import static com.excilys.formation.tbezenger.cdb.Strings.COMPUTER_NAME;
import static com.excilys.formation.tbezenger.cdb.Strings.COMPUTER_DISCONTINUED;
import static com.excilys.formation.tbezenger.cdb.Strings.COMPUTER_INTRODUCED;


@Repository
public class ComputerManager implements EntityManager<Computer> {

    private javax.persistence.EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;
	public ComputerManager(JdbcTemplate jdbcTemplate, javax.persistence.EntityManager entityManager) {
		this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
	}

	private final static String FIND_BY_ID_QUERY = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
			+ "company.id,company.name FROM computer "
			+ "LEFT JOIN company ON company_id=company.id WHERE computer.id=?";

	private final static String FIND_ALL_QUERY = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
			+ "company.id,company.name FROM computer " + "LEFT JOIN company ON company_id=company.id";

	private final static String PERSIST_QUERY = "INSERT INTO computer (name,introduced,discontinued,company_id) values"
			+ "(?,?,?,?);";

	private final static String DELETE_QUERY = "DELETE FROM computer WHERE id=?";

	private final static String DELETE_QUERY_BY_COMPANY_ID = "DELETE FROM computer WHERE company_id=?";

	private final static String UPDATE_QUERY = "UPDATE computer SET name=?,introduced=?,discontinued=?,company_id=? "
			+ "WHERE id=?";


	private final static String GET_COMPUTERS_COUNT = "SELECT count(*) FROM computer LEFT JOIN company ON company_id=company.id"
			+ " WHERE computer.name LIKE ? OR "
			+ "company.name LIKE ?";

	private final static String GET_COMPUTERS = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,"
			+ "company.id,company.name FROM computer LEFT JOIN company ON company_id=company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY %s %s LIMIT ?,?";


	@Override
	public Optional<Computer> findById(int id) throws DatabaseException {
		Computer computer = null;
		try {
            jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,
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
	public ComputerPage findPage(int numpage, int rowsByPage, String search, String orderBy, boolean isAscending) throws DatabaseException {
		String getQuery = String.format(GET_COMPUTERS, orderBy, isAscending ? "ASC" : "DESC");
		ComputerPage page = new ComputerPage();
        System.out.println("EM : " + entityManager);
        try {
			page.setTotalResults(jdbcTemplate.queryForObject(GET_COMPUTERS_COUNT, new Object[]{"%" + search + "%", "%" + search + "%"}, Integer.class));
			page.setMaxPage(page.getTotalResults() / rowsByPage + 1);
			numpage = numpage <= page.getMaxPage() ? numpage : page.getMaxPage();
			page.setComputers(jdbcTemplate.query(getQuery, new Object[]{"%" + search + "%", "%" + search + "%"/*, orderBy, order*/, (numpage - 1) * rowsByPage, rowsByPage},
												 new RowMapper<Computer>() {
				public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
					Company company = new Company(rs.getInt(COMPANY_ID), rs.getString(COMPANY_NAME));
					Computer computer = new Computer(rs.getInt(COMPUTER_ID), rs.getString(COMPUTER_NAME), rs.getDate(COMPUTER_INTRODUCED),
							rs.getDate(COMPUTER_DISCONTINUED), company);
					return computer;
				}
			}));
			page.setNumPage(numpage);
			page.setRows(rowsByPage);
			page.setSearch(search);
			page.setOrderBy(orderBy);
			page.setIsAscending(isAscending);
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

	boolean removeByCompanyId(int companyId) throws DatabaseException {
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
								computer.getDiscontinued(),
								computer.getCompany().getId(), computer.getId());
		} catch (DataAccessException e) {
			e.printStackTrace();
			LOGGER.error(e.toString());
			throw (new UpdateException());
		}
		return true;
	}
}