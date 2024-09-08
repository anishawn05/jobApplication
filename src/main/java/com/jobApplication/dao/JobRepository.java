package com.jobApplication.dao;

import com.jobApplication.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> , JpaSpecificationExecutor<Job> {
    List<Job> getBylocation(String location);
    // Native Query
    @Query(value = "SELECT * FROM Job_Application WHERE location = :location", nativeQuery = true)
    List<Job> getByLocationNativeQuery(String location);


    List<Job> findAllByLocation(String loc);

}