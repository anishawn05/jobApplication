package com.jobApplication.controller;

import com.jobApplication.Exception.*;
import com.jobApplication.audit.Auditable;
import com.jobApplication.company.Company;
import com.jobApplication.company.CompanyController;
import com.jobApplication.company.CompanyService;
import com.jobApplication.model.Job;
import com.jobApplication.service.JobService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
//@CrossOrigin(origins = "http://localhost:4200")
public class JobController {
    @Autowired
    JobService jobService;



    @GetMapping("/getAllJob")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Job> getAllJOb() {
        return jobService.getAll();
    }


    @PostMapping(value = "/create/{companyId}")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Job> createJob(@RequestBody Job job, @PathVariable Long companyId) {
        try {
            Job createdJob = jobService.createJob(job, companyId);
            return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
        } catch (CompanyNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

        @GetMapping("/{field}")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Job> getAllBySorting(@PathVariable String field) {
        return jobService.getAllBySorting(field);
    }

    @GetMapping("paging/{offset}/{pageSize}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Page<Job> pagination(@PathVariable int offset, @PathVariable int pageSize) {
        return jobService.paginationJob(offset, pageSize);
    }

    @GetMapping("paginationWithSorting/{offset}/{pageSize}/{field}")
    public Page<Job> paginationWithSorting(@PathVariable int offset, @PathVariable int pageSize, @PathVariable String field) {
        return jobService.paginationJobWithSorting(offset, pageSize, field);
    }




    @GetMapping("/getById/{id}")
    public Job getJobById(@PathVariable Long id) throws InValidJobIdException {
        Job b = jobService.getJobById(id);
        return b;

    }

    @PutMapping("/update/{id}")
    @Auditable
    public ResponseEntity<Job> updateJob(@RequestBody Job updatedJob, @PathVariable Long id) {
        try {
            Job updatedJobResponse = jobService.update(updatedJob, id);
            return ResponseEntity.ok(updatedJobResponse);
        } catch (UpdationNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        jobService.delete(id);
        return "Delete Success";
    }

    @GetMapping("/getByLocation/{location}")
    public ResponseEntity<Object> getByLocation(@PathVariable String location) {
        try {
            List<Job> jobs = jobService.getByLocation(location);
            return ResponseEntity.ok(jobs);
        } catch (JobNotFoundException e) {
            // Handle the exception gracefully, return a custom error response
            JobApplicationException exceptionResponse = new JobApplicationException("Location not accepted");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);

        }
    }

    @GetMapping("/getByLocationNativeQuery")
    public ResponseEntity<Object> getByLocationNativeQuery(@RequestParam String location) throws JobNotFoundException {
        List<Job> jobs = jobService.getByLocationNativeQuery(location);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/getTitleOfJob")
    public List<String> getTitle() {
        return jobService.getTitle();

    }

    @GetMapping("/findByMinSalary")
    public List<Job> findByMinSalary(@RequestParam("minSalary") String minSalary, @RequestParam("location") String location) {
        return jobService.findByMinSalary(minSalary, location);
    }


}