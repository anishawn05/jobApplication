package com.jobApplication.company;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@Hidden// for swagger dosent view
public class CompanyController {
    @Autowired
    CompanyServiceImpl companyServiceImpl;

    @GetMapping("/getAllCompany")
    public List<Company> getAllCompany() {
        return companyServiceImpl.getAllCompany();
    }

    @PostMapping("/createCompany")
    public ResponseEntity<Company> createCompany(@RequestBody Company company){
        Company createdCompany = companyServiceImpl.creatCompany(company);
        return new ResponseEntity<>(createdCompany , HttpStatus.CREATED);
    }
    @GetMapping("/getCompanyName")
    public ResponseEntity<String> getCompanyName(@RequestParam String name) {
        String companyName = companyServiceImpl.getCompanyName(name);
        if (companyName != null) {
            return ResponseEntity.ok(companyName);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/updateCompany/{id}")
    public Company updateCompany(@PathVariable Long id, @RequestBody Company company) {
        return companyServiceImpl.updateCompany(id, company);
    }


}