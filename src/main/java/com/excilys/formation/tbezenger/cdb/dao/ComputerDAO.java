package com.excilys.formation.tbezenger.cdb.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

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
public class ComputerDAO implements DAO<Computer> {
	@PersistenceContext
    private javax.persistence.EntityManager em;

    private final JdbcTemplate jdbcTemplate;
	public ComputerDAO(JdbcTemplate jdbcTemplate, EntityManagerFactory entityManagerFactory) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private CriteriaBuilder cb;

    @PostConstruct
    public void init() {
        this.cb = em.getCriteriaBuilder();
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
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY %s %s LIMIT ?,?";


	@Override
	@Transactional
	public Optional<Computer> findById(int id) throws DatabaseException {
		Computer computer = null;
		try {
			CriteriaQuery<Computer> criteriaQuery = cb.createQuery(Computer.class);
			Root<Computer> model = criteriaQuery.from(Computer.class);
			criteriaQuery.where(cb.equal(model.get("id"), id));
			TypedQuery<Computer> query = em.createQuery(criteriaQuery);
			computer = query.getSingleResult();
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return Optional.ofNullable(computer);
	}


	// TODO
	@Override
	@Transactional
	public List<Computer> findall() throws DatabaseException {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			CriteriaQuery<List<Computer> criteriaQuery = cb.createQuery(Computer.class);
			
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

	//TODO
	@Transactional
	public ComputerPage findPage(int numpage, int rowsByPage, String search, String orderBy, boolean isAscending) throws DatabaseException {
		String getQuery = String.format(GET_COMPUTERS, orderBy, isAscending ? "ASC" : "DESC");
		ComputerPage page = new ComputerPage();
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
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return page;
	}

	@Transactional
	public boolean persist(Computer computer) throws DatabaseException {
		try {
			em.persist(computer);
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new PersistException());
		}
		return true;
	}

	//TODO
	@Transactional
	public boolean remove(int id) throws DatabaseException {
		try {
			jdbcTemplate.update(DELETE_QUERY, id);
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new DeleteException());
		}
		return true;
	}

	//TODO
	boolean removeByCompanyId(int companyId) throws DatabaseException {
		try {
			jdbcTemplate.update(DELETE_QUERY_BY_COMPANY_ID, companyId);
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new DeleteException());
		}
		return true;
	}

	@Transactional
	public boolean update(Computer computer) throws DatabaseException {
		try {
			CriteriaUpdate<Computer> update = cb.createCriteriaUpdate(Computer.class);
			Root<Computer> model = update.from(Computer.class);
			update.set("name", computer.getName());
			update.set("introduced", computer.getIntroduced());
			update.set("discontinued", computer.getDiscontinued());
			if (computer.getCompany() != null) {
				update.set("company", computer.getCompany());
			}
			update.where(cb.equal(model.get("id"), computer.getId()));
			em.createQuery(update).executeUpdate();
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new UpdateException());
		}
		return true;
	}
}