package com.jobApplication.dao;


import com.jobApplication.model.CandidateImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandidateImageRepository extends JpaRepository<CandidateImage,Long> {
   Optional<CandidateImage>  findByName(String fileName);

}
