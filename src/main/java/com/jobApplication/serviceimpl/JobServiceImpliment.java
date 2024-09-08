package com.jobApplication.serviceimpl;

import com.jobApplication.Exception.CompanyNotFoundException;
import com.jobApplication.Exception.InValidJobIdException;
import com.jobApplication.Exception.JobNotFoundException;
import com.jobApplication.Exception.UpdationNotFound;
import com.jobApplication.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface JobServiceImpliment {
    List<Job> getAll();
    Job createJob(Job job, Long companyId) throws CompanyNotFoundException;

    Job getJobById(Long id) throws  InValidJobIdException;

    Job update(Job updatedJob, Long id) throws UpdationNotFound;


    String delete(Long id);

    List<Job> getByLocation(String location) throws JobNotFoundException;

    List<Job> getByLocationNativeQuery(String location) throws JobNotFoundException;

    List<Job> getAllBySorting(String field);

    Page<Job> paginationJob(int offset, int pageSize);

    Page<Job> paginationJobWithSorting(int offset, int pageSize, String field);

    List<Job> findByMinSalary(String minSalary , String location);




    List<String> getTitle();


    Integer uploadFile(MultipartFile file) throws IOException;
}