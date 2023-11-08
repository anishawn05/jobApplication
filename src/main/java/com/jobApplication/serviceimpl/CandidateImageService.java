package com.jobApplication.serviceimpl;


import com.jobApplication.Exception.ImageNotFoundException;
import com.jobApplication.model.CandidateImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CandidateImageService {

    String uploadImage(MultipartFile file) throws IOException, ImageNotFoundException;

    byte[] getImmage(String fileName);


    byte[] getImageById(Long id);

    List<CandidateImage> getAllImmage();
}
