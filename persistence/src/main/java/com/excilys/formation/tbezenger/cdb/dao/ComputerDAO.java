package com.excilys.formation.tbezenger.cdb.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.GetException;
import com.excilys.formation.tbezenger.cdb.exceptions.DAO.PersistException;
import com.excilys.formation.tbezenger.cdb.exceptions.DAO.UpdateException;
import com.excilys.formation.tbezenger.cdb.model.ComputerPage;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DeleteException;
import com.excilys.formation.tbezenger.cdb.model.Computer;


@Repository
public class ComputerDAO implements DAO<Computer> {
	@PersistenceContext
    private EntityManager em;
	private CriteriaBuilder cb;

    @PostConstruct
    public void init() {
        this.cb = em.getCriteriaBuilder();
    }

	@Override
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


	public long findRelevantComputersCount(String search) throws DatabaseException {
		try {
			CriteriaQuery<Long> criteriaQuery2 = cb.createQuery(Long.class);
			Root<Computer> model = criteriaQuery2.from(Computer.class);
			criteriaQuery2.select(cb.count(model));
			criteriaQuery2.where(cb.like(model.get("name"), "%" + search + "%"));
			TypedQuery<Long> query2 = em.createQuery(criteriaQuery2);
			return query2.getSingleResult();
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
	}


	public List<Computer> findRelevantComputers(ComputerPage page) throws DatabaseException {
        try {
			CriteriaQuery<Computer> criteriaQuery = cb.createQuery(Computer.class);
			Root<Computer> model = criteriaQuery.from(Computer.class);
			if (page.getIsAscending()) {
				if (page.getOrderBy().equals("company")) {
					criteriaQuery.orderBy(cb.asc(model.get("company").get("name")));
				} else {
					criteriaQuery.orderBy(cb.asc(model.get(page.getOrderBy())));
				}
			} else {
				if (page.getOrderBy().equals("company")) {
					criteriaQuery.orderBy(cb.desc(model.get("company").get("name")));
				} else {
					criteriaQuery.orderBy(cb.desc(model.get(page.getOrderBy())));
				}
			}
			criteriaQuery.where(cb.like(model.get("name"), "%" + page.getSearch() + "%"));
			TypedQuery<Computer> query = em.createQuery(criteriaQuery);
			query.setFirstResult((page.getNumPage() - 1) * page.getRows());
			query.setMaxResults(page.getRows());
			return query.getResultList();
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
	}

	
	public boolean persist(Computer computer) throws DatabaseException {
		try {
			em.persist(computer);
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new PersistException());
		}
		return true;
	}

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

	public boolean removeByCompanyId(int companyId) throws DatabaseException {
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