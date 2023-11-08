package com.jobApplication.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    CompanyServiceImpl companyServiceImpl;

    @GetMapping("/getAllCompany")
    public List<Company> getAllCompany() {
        return companyServiceImpl.getAllCompany();
    }
    @PostMapping("/posting")
    public String createCompany(@RequestBody Company company){
        companyServiceImpl.creatCompany(company);
        return "create company successes ";
    }

    @PutMapping("/updateCompany/{id}")
    public Company updateCompany(@PathVariable Long id, @RequestBody Company company) {
        return companyServiceImpl.updateCompany(id, company);
    }


}
