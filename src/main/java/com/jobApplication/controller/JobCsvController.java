package com.jobApplication.controller;

import com.jobApplication.Exception.ImageNotFoundException;
import com.jobApplication.service.CandidateImageServiceImpl;
import com.jobApplication.service.JobService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('CLIENT')")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class JobCsvController {
    private final JobService jobService;
    @PostMapping(value = "/upload",consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('client:create')")
    public ResponseEntity<Integer> upload(@RequestPart ("file")MultipartFile file) throws IOException {
        return ResponseEntity.ok(jobService.uploadFile(file));

    }
}
