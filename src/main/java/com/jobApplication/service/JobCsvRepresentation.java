package com.jobApplication.service;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobCsvRepresentation {
    @CsvBindByName(column = "title")
    private String title;
    @CsvBindByName(column = "description")
    private String description;
    @CsvBindByName(column = "minSalary")
    private String mSalary;
    @CsvBindByName(column = "maxSalary")
    private String maSalary;
    @CsvBindByName(column = "location")
    private String location;
}
