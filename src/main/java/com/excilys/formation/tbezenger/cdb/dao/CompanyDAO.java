package com.excilys.formation.tbezenger.cdb.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.GetException;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.excilys.formation.tbezenger.cdb.exceptions.DAO.DatabaseException;
import com.excilys.formation.tbezenger.cdb.model.Company;

@Repository
public class CompanyDAO implements DAO<Company> {

	@PersistenceContext
    private javax.persistence.EntityManager em;

	private CriteriaBuilder cb;

    @PostConstruct
    public void init() {
        this.cb = em.getCriteriaBuilder();
    }

    @Override
    public List<Company> findall() throws DatabaseException {
        List<Company> companies = new ArrayList<Company>();
        try {
        	CriteriaQuery<Company> criteriaQuery = cb.createQuery(Company.class);
			criteriaQuery.from(Company.class);
			companies = em.createQuery(criteriaQuery).getResultList();
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
			CriteriaQuery<Company> criteriaQuery = cb.createQuery(Company.class);
			Root<Company> model = criteriaQuery.from(Company.class);
			criteriaQuery.where(cb.equal(model.get("id"), id));
			company = em.createQuery(criteriaQuery).getSingleResult();
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return Optional.ofNullable(company);
	}

	public boolean remove(int id) throws DatabaseException {
		try {
			CriteriaDelete<Company> criteriaQuery = cb.createCriteriaDelete(Company.class);
			Root<Company> model = criteriaQuery.from(Company.class);
			criteriaQuery.where(cb.equal(model.get("id"), id));
			em.createQuery(criteriaQuery).executeUpdate();
		} catch (DataAccessException e) {
			LOGGER.error(e.toString());
			throw (new GetException());
		}
		return true;
	}
}