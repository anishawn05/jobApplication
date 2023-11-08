package com.jobApplication.service;

import com.jobApplication.Exception.InValidJobIdException;
import com.jobApplication.Exception.JobNotFoundException;
import com.jobApplication.dao.JobRepository;
import com.jobApplication.model.Job;
import com.jobApplication.serviceimpl.JobServiceImpliment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService implements JobServiceImpliment {
    @Autowired
    JobRepository jobRepository;

    @Override
    public List<Job> getAll() {

        return jobRepository.findAll();
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
    public Job create(Job e) {
        return jobRepository.save(e);

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
    public Job update(Job e, Long id) {

        return  jobRepository.save(e);
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


}
