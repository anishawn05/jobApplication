package com.jobApplication.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jobApplication.model.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @JsonIgnore
    @OneToMany (mappedBy = "company")// one company have many jobs,
    private List<Job> jobs;
}
