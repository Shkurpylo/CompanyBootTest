package com.manager.service;

import com.manager.dao.ICompanyDAO;
import com.manager.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class CompanyService implements ICompanyService {
	@Autowired
	private ICompanyDAO companyDAO;
	@Override
	public Company getCompanyById(int cid) {
		Company obj = companyDAO.getCompanyById(cid);
		return obj;
	}

	@Override
	public List<Company> getAllCompanies(){
		List <Company> companiesList = companyDAO.getAllCompanies();
		setDepth(companiesList);
		showHierarchy(companiesList);
		return companiesList;
	}

	@Override
	public synchronized boolean addCompany(Company company){
		if(!companyDAO.personExists(company.getName())) {
			boolean isParent = company.getParentsName() == null;
			boolean isFirst = getAllCompanies().size() == 0;
			if (isFirst) {
				setTreeCoordinatesForFirstCompany(company);
				company.setTotalEstimatedEarnings(countTotalEarnings(company));
				companyDAO.addParentCompany(company);
			} else if (isParent) {
				setTreeCoordinatesForParent(company);
				company.setTotalEstimatedEarnings(countTotalEarnings(company));
				companyDAO.addParentCompany(company);
				rebuildTree(company);
			} else {
				companyDAO.addSubCompany(company);
				rebuildTree(company);
				countTotalEarningsForAll(companyDAO.getAllCompanies());
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updateCompany(Company company) {
		companyDAO.updateCompany(company);
		countTotalEarningsForAll(companyDAO.getAllCompanies());
	}

	@Override
	public void deleteCompany(int id) {
		boolean isParent = companyDAO.getCompanyById(id).getParentsName() == null;
		if(isParent) {
			deleteParentCompanyFromTree(companyDAO.getCompanyById(id));
		} else {
			deleteSubCompanyFromTree(companyDAO.getCompanyById(id));
		}
		countTotalEarningsForAll(companyDAO.getAllCompanies());
	}


	private void countTotalEarningsForAll(List<Company> companiesList){
		for(Company company : companiesList){
			company.setTotalEstimatedEarnings(countTotalEarnings(company));
			companyDAO.updateCompany(company);
		}
	}

	private long countTotalEarnings(Company company){
		long selfEarnings = company.getSelfEstimatedEarnings();
		if(company.getRgt() - company.getLft() == 1) {
			return selfEarnings;
		} else {
			List<Company> companyList = companyDAO.getAllCompanies();
			for(Company iterCompany : companyList){
				if(iterCompany.getLft() > company.getLft() && iterCompany.getRgt() < company.getRgt()) {
					selfEarnings += iterCompany.getSelfEstimatedEarnings();
				}
				if(iterCompany.getLft() > company.getRgt()){
					return selfEarnings;
				}
			}
		}
		return selfEarnings;
	}

	private void setTreeCoordinatesForFirstCompany(Company company) {
		company.setLft(1);
		company.setRgt(2);
	}

	private void setTreeCoordinatesForParent(Company company){
		if(getAllCompanies().size() == 0){
			company.setLft(1);
			company.setRgt(2);
		} else {
			int highestRgt = getHighestRgt(getAllCompanies());
			company.setLft(highestRgt + 1);
			company.setRgt(highestRgt + 2);
		}
	}

	private int getHighestRgt(List<Company> companiesList) {
		int highestRgt = 0;
		for(Company company : companiesList) {
			highestRgt = company.getRgt() > highestRgt ? company.getRgt() : highestRgt;
		}
		return highestRgt;
	}

	private void deleteSubCompanyFromTree(Company company){
		List<Company> companiesList = companyDAO.getAllCompanies();
		for(Company iterCompany : companiesList){
			int iterLft = iterCompany.getLft();
			int iterRgt = iterCompany.getRgt();
			if(iterLft > company.getLft() && iterRgt < company.getRgt()){
				iterCompany.setLft(iterLft - 1);
				iterCompany.setRgt(iterRgt - 1);
			} else if(iterLft > company.getLft() && iterRgt > company.getRgt()){
				iterCompany.setLft(iterLft - 2);
				iterCompany.setRgt(iterRgt - 2);
			} else if(iterLft < company.getLft() && iterRgt > company.getRgt()){
				iterCompany.setRgt(iterRgt - 2);
			}
			companyDAO.updateCompany(iterCompany);
		}
		companyDAO.deleteCompany(company.getId());
	}

	private void deleteParentCompanyFromTree(Company company){
		List<Company> companiesList = companyDAO.getAllCompanies();
		for(Company iterCompany : companiesList){
			int iterLft = iterCompany.getLft();
			int iterRgt = iterCompany.getRgt();
			if(iterLft >= company.getLft() && iterRgt <= company.getRgt()){
				companyDAO.deleteCompany(iterCompany.getId());
			} else if (iterLft > company.getLft() && iterRgt > company.getRgt()) {
				iterCompany.setLft(iterLft - company.getRgt());
				iterCompany.setRgt(iterRgt - company.getRgt());
				companyDAO.updateCompany(iterCompany);
			}
		}
	}

	private void rebuildTree(Company newCompany){
		int newCompanyLft = newCompany.getLft();
		int newCompanyRgt = newCompany.getRgt();
		List<Company> companiesList = getAllCompanies();

		for(Company iterCompany : companiesList){
			if(iterCompany.equals(newCompany)){
				continue;
			} else {
				if (iterCompany.getRgt() == newCompanyLft){
					iterCompany.setRgt(iterCompany.getRgt()+2);
				} else if(iterCompany.getLft() >= newCompanyRgt) {
					iterCompany.setLft(iterCompany.getLft() + 2);
					iterCompany.setRgt(iterCompany.getRgt() + 2);
				}else if (iterCompany.getLft() == newCompanyLft){
					iterCompany.setLft(iterCompany.getLft() + 2);
					iterCompany.setRgt(iterCompany.getRgt() + 2);
				} else if (iterCompany.getRgt() >= newCompanyRgt && iterCompany.getLft() <= newCompanyLft){
					iterCompany.setRgt(iterCompany.getRgt() + 2);
				}
				companyDAO.updateCompany(iterCompany);
			}
		}
	}

	private void setDepth(List<Company> companyList){
		Company parent;
		try {
			parent = companyList.get(0);
		} catch (IndexOutOfBoundsException ex){
			return;
		}

		int levelCount = 0;
		for (int i = 1; i < companyList.size(); i++) {

			Company current = companyList.get(i);
			if (current.getLft() - parent.getLft() == 1) {
				levelCount++;
				parent = current;
				parent.setDepth(levelCount);
				continue;
			} else if (current.getLft() - parent.getLft() == 2) {
				current.setDepth(levelCount);
				parent = current;
				parent.setDepth(levelCount);
			} else if (current.getLft() - parent.getLft() > 2
					&& parent.getRgt() - current.getLft() == 1){
				levelCount = 0;
				parent = current;
				parent.setDepth(levelCount);
			}
		}
	}

	private void showHierarchy(List<Company> companiesList){

		for(Company company : companiesList){
			StringBuilder lines = new StringBuilder();
			for(int i = 0; i < company.getDepth(); i++){
				lines.append("‒ ");
			}
			company.setVisual(lines.toString());
			companyDAO.updateCompany(company);
		}
	}
}
