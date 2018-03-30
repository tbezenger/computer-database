package com.excilys.formation.tbezenger.cdb.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.GetException;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.cdb.model.Company;

import static com.excilys.formation.tbezenger.cdb.Strings.COMPANY_ID;
import static com.excilys.formation.tbezenger.cdb.Strings.COMPANY_NAME;

@Repository
public class CompanyDAO implements DAO<Company> {

    private JdbcTemplate jdbcTemplate;
    private ComputerDAO computerManager;
	public CompanyDAO(JdbcTemplate jdbcTemplate, ComputerDAO computerManager) {
		this.jdbcTemplate = jdbcTemplate;
		this.computerManager = computerManager;
	}

    private final String FIND_ALL_QUERY = "SELECT id,name FROM company";
    private final String FIND_BY_ID_QUERY = "SELECT id,name FROM company WHERE id=?";
    private final String DELETE_COMPANY_BY_ID = "DELETE FROM company WHERE id=?";

    @Override
    public List<Company> findall() throws DatabaseException {
        List<Company> companies = new ArrayList<Company>();
        try {
        	companies = jdbcTemplate.query(FIND_ALL_QUERY, new RowMapper<Company>() {
        								public Company mapRow(ResultSet rs, int rownum) throws SQLException {
        									return new Company(rs.getInt(COMPANY_ID), rs.getString(COMPANY_NAME));
        								}
        	});
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return companies;
	}

	@Override
	public Optional<Company> findById(int id) throws DatabaseException {
		Company company = null;
		try {
			company = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new Object[] {id}, new RowMapper<Company>() {
											public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
												return new Company(rs.getInt(COMPANY_ID), rs.getString(COMPANY_NAME));
											}
			});
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return Optional.ofNullable(company);
	}

	public boolean remove(int id) throws DatabaseException {
		try {
			computerManager.removeByCompanyId(id);
			jdbcTemplate.update(DELETE_COMPANY_BY_ID, id);
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return true;
	}
}