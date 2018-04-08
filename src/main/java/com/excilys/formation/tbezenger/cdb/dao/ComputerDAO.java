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
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;

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
	private CriteriaBuilder cb;

    @PostConstruct
    public void init() {
        this.cb = em.getCriteriaBuilder();
    }

	@Override
	@Transactional
	public Optional<Computer> findById(int id) throws DatabaseException {
		Computer computer;
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

	@Override
	@Transactional
	public List<Computer> findall() throws DatabaseException {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			CriteriaQuery<Computer> criteriaQuery = cb.createQuery(Computer.class);
			criteriaQuery.from(Computer.class);
			computers = em.createQuery(criteriaQuery).getResultList();
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return computers;
	}

	@Transactional
	public ComputerPage findPage(int numPage, int rowsByPage, String search, String orderBy, boolean isAscending) throws DatabaseException {
		ComputerPage page = new ComputerPage();
        try {
			CriteriaQuery<Long> criteriaQuery2 = cb.createQuery(Long.class);
			Root<Computer> model = criteriaQuery2.from(Computer.class);
			criteriaQuery2.select(cb.count(model));
			criteriaQuery2.where(cb.like(model.get("name"), "%" + search + "%"));
			TypedQuery<Long> query2 = em.createQuery(criteriaQuery2);
			page.setTotalResults(query2.getSingleResult().intValue());
			page.setMaxPage(page.getTotalResults() / rowsByPage + 1);
			numPage = numPage <= page.getMaxPage() ? numPage : page.getMaxPage();


			CriteriaQuery<Computer> criteriaQuery = cb.createQuery(Computer.class);
			model = criteriaQuery.from(Computer.class);
			if (isAscending) {
				if (orderBy.equals("company")){
					criteriaQuery.orderBy(cb.asc(model.get("company").get("name")));
				} else {
					criteriaQuery.orderBy(cb.asc(model.get(orderBy)));
				}
			} else {
				if (orderBy.equals("company")){
					criteriaQuery.orderBy(cb.asc(model.get("company").get("name")));
				} else {
					criteriaQuery.orderBy(cb.asc(model.get(orderBy)));
				}
			}
			criteriaQuery.where(cb.like(model.get("name"), "%" + search + "%"));
			TypedQuery<Computer> query = em.createQuery(criteriaQuery);
			query.setFirstResult((numPage - 1) * rowsByPage);
			query.setMaxResults(rowsByPage);
			page.setComputers(query.getResultList());

			page.setNumPage(numPage);
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

	@Transactional
	public boolean remove(int id) throws DatabaseException {
		try {
			CriteriaDelete<Computer> criteriaQuery = cb.createCriteriaDelete(Computer.class);
			Root<Computer> model = criteriaQuery.from(Computer.class);
			criteriaQuery.where(cb.equal(model.get("id"), id));
			em.createQuery(criteriaQuery).executeUpdate();
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new DeleteException());
		}
		return true;
	}

	boolean removeByCompanyId(int companyId) throws DatabaseException {
		try {
			CriteriaDelete<Computer> criteriaQuery = cb.createCriteriaDelete(Computer.class);
			Root<Computer> model = criteriaQuery.from(Computer.class);
			criteriaQuery.where(cb.equal(model.get("company_id"), companyId));
			em.createQuery(criteriaQuery).executeUpdate();
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