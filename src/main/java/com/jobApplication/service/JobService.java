package com.jobApplication.service;

import com.jobApplication.Exception.CompanyNotFoundException;
import com.jobApplication.Exception.InValidJobIdException;
import com.jobApplication.Exception.JobNotFoundException;
import com.jobApplication.Exception.UpdationNotFound;
import com.jobApplication.audit.Auditable;
import com.jobApplication.company.Company;
import com.jobApplication.company.CompanyRepository;
import com.jobApplication.company.CompanyService;
import com.jobApplication.dao.JobRepository;
import com.jobApplication.model.Job;
import com.jobApplication.serviceimpl.JobServiceImpliment;
import com.jobApplication.specification.JobSpecification;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobService implements JobServiceImpliment {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    CompanyService companyService;
    @Override
    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    @Override
    public Job createJob(Job job, Long companyId) throws CompanyNotFoundException {
        // Find the company by ID
        Company company = companyService.findById(companyId);
        if (company == null) {
            throw new CompanyNotFoundException("Company not found with ID: " + companyId);
        }

        // Set the company for the job
        job.setCompany(company);

        // Save the job
        return jobRepository.save(job);
    }


    @Override
    public List<Job> getAllBySorting(String field) {
        return jobRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    @Override
    public Page<Job> paginationJob(int offset, int pageSize) {
        return jobRepository.findAll(PageRequest.of(offset, pageSize));
    }

    @Override
    public Page<Job> paginationJobWithSorting(int offset, int pageSize, String field) {
        return jobRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
    }

    @Override
    public List<Job> findByMinSalary(String minSalary, String location) {
        Specification<Job> specification = Specification.where(JobSpecification.hasMinSalary(minSalary));
        // .and(JobSpecification.locationLike(location));
        return jobRepository.findAll(specification);
    }



    @Override
    public Job getJobById(Long id) throws InValidJobIdException {


        if (id % 2 == 0) {
            Optional<Job> j = jobRepository.findById(id);
            return j.get();
        } else {
            throw new InValidJobIdException("This Id Is Not Accepted");
        }

    }

    @Override
    public Job update(Job updatedJob, Long id) throws UpdationNotFound {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        // Update fields
        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setMinSalary(updatedJob.getMinSalary());
        existingJob.setMaxSalary(updatedJob.getMaxSalary());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setCompany(updatedJob.getCompany());

        return jobRepository.save(existingJob);
    }


    @Override
    public String delete(Long id) {
        jobRepository.deleteById(id);
        return "return delete";
    }

    @Override
    public List<Job> getByLocation(String location) throws JobNotFoundException {
        if (location.equals("india")) {
            return jobRepository.getBylocation(location);
        } else {
            throw new JobNotFoundException("not accepted");
        }
    }

    public List<Job> getByLocationNativeQuery(String location) throws JobNotFoundException {
        if (!location.isEmpty()) {
            return jobRepository.getByLocationNativeQuery(location);
        } else {
            throw new JobNotFoundException("job not in there ");
        }
    }

    @Override
    public List<String> getTitle() {
        List<Job> abc = jobRepository.findAll();
        return abc.stream().map(Job::getTitle).filter(a -> a.contains("English")).collect(Collectors.toList());
    }


    public Integer uploadFile(MultipartFile file) throws IOException {
        Set<Job> jobs = parseCsv(file);
        jobRepository.saveAll(jobs);
        return jobs.size();
    }




    private Set<Job> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<JobCsvRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(JobCsvRepresentation.class);
            CsvToBean<JobCsvRepresentation> csvToBean = new CsvToBeanBuilder<JobCsvRepresentation>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> Job.builder()
                            .title(csvLine.getTitle())
                            .description(csvLine.getDescription())
                            .minSalary(csvLine.getMSalary())
                            .maxSalary(csvLine.getMaSalary())
                            .location(csvLine.getLocation())
                            .build()
                    )
                    .collect(Collectors.toSet());


        }
    }


}