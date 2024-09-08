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
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Override
    public String toString() {
        return "Job(id=" + id + ", title=" + title + ", description=" + description +
                ", minSalary=" + minSalary + ", maxSalary=" + maxSalary +
                ", location=" + location + ", company=" + (company != null ? company.getName() : "null") + ")";
    }

}