package com.jobApplication.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Override
    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }

    @Override
    public Company creatCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(Long id, Company company) {
        Company existingCompany = companyRepository.findById(id).orElse(null);
        if (existingCompany == null) {
            return null;
        }

        // Update the existing company with the new values.
        existingCompany.setName(company.getName());
        existingCompany.setDescription(company.getDescription());
        existingCompany.setJobs(company.getJobs());

        // Save the updated company.
        return companyRepository.save(existingCompany);
    }
    }
