package com.jobApplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jobApplication.company.Company;
import lombok.*;

import javax.persistence.*;
import java.util.stream.DoubleStream;

@Entity
@Table(name = "JobApp")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String minSalary;
    private String maxSalary;
    private String location;
    @ManyToOne//Many jobs link to one company
   // @JsonIgnoreProperties("jobs") // Ignore the 'jobs' field in the Company class during serialization
    private Company company;



}
