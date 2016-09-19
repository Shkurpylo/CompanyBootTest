package com.manager.dao;

import com.manager.entity.Company;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

import java.util.List;

@Transactional
@Repository
public class CompanyDAO implements ICompanyDAO {
	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Company getCompanyById(int pid) {
		return (Company) getCurrentSession().get(Company.class, pid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Company> getAllCompanies() {
		String hql = "FROM Company ORDER BY lft";
		List<Company> companiesList = getCurrentSession().createQuery(hql).list();
		return companiesList;
	}

	@Override
	public boolean addParentCompany(Company company) {
		getCurrentSession().save(company);
		return false;
	}

	@Override
	public boolean addSubCompany(Company company) {
		String parentName = company.getParentsName();
		String hql = String.format("FROM Company WHERE name = '%s'", parentName);
		Query query = getCurrentSession().createQuery(hql);
		query.setMaxResults(1);
		Company parent = (Company) query.uniqueResult();
		company.setLft(parent.getLft() + 1);
		company.setRgt(company.getLft() + 1);
		getCurrentSession().save(company);

		return false;
	}

	@Override
	public void updateCompany(Company company) {
		Company p = getCompanyById(company.getId());
		p.setName(company.getName());
		p.setSelfEstimatedEarnings(company.getSelfEstimatedEarnings());
		p.setRgt(company.getRgt());
		p.setLft(company.getLft());
		p.setTotalEstimatedEarnings(company.getTotalEstimatedEarnings());
		p.setVisual(company.getVisual());
		getCurrentSession().update(p);
	}

	@Override
	public void deleteCompany(int id) {
		getCurrentSession().delete(getCompanyById(id));
	}

	@Override
	public boolean personExists(String name) {
		String hql = String.format("FROM Company WHERE name = '%s'", name);
		Query query = getCurrentSession().createQuery(hql);
		query.setMaxResults(1);
		return query.uniqueResult() != null;
	}


}
