package com.manager.service;

import com.manager.entity.Company;

import java.util.List;

public interface ICompanyService {
     List<Company> getAllCompanies();
     Company getCompanyById(int pid);
     boolean addCompany(Company company);
     void updateCompany(Company company);
     void deleteCompany(int pid);
}
