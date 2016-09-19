package com.manager.dao;

import com.manager.entity.Company;
import java.util.List;

public interface ICompanyDAO {
    List<Company> getAllCompanies();
    Company getCompanyById(int pid);
    boolean addParentCompany(Company company);
    boolean addSubCompany(Company company);
    void updateCompany(Company company);
    void deleteCompany(int pid);
    boolean personExists(String name);
}
 