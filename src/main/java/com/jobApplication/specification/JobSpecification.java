package com.jobApplication.specification;

import com.jobApplication.model.Job;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class JobSpecification {
    public static Specification<Job> hasMinSalary(String minSalary){
        return (Root<Job> root , CriteriaQuery<?> query , CriteriaBuilder builder) ->{
           int salary = Integer.parseInt(minSalary);
            if(salary > 80000 && salary < 90000 ){
                return null;
            }
            return builder.equal(root.get("minSalary"), minSalary);
        };
    }

    public static Specification<Job> locationLike(String location){
        return (Root<Job> root , CriteriaQuery<?> query , CriteriaBuilder builder) -> {
            if(location == null){
                return null;
            }
            return builder.like(root.get("location") , "%" + location + "%");
        };
    }
}
