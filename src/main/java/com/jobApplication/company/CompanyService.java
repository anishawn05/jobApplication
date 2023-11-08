package com.jobApplication.company;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface CompanyService {

    List<Company> getAllCompany();

    Company creatCompany(Company company);

    Company updateCompany(Long id, Company company);
}
