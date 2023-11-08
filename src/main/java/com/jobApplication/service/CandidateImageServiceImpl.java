package com.jobApplication.service;

import com.jobApplication.Exception.ImageNotFoundException;
import com.jobApplication.dao.CandidateImageRepository;
import com.jobApplication.model.CandidateImage;
import com.jobApplication.serviceimpl.CandidateImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateImageServiceImpl implements CandidateImageService {
    @Autowired
    CandidateImageRepository candidateImageRepository;


    @Override
    public String uploadImage(MultipartFile file) throws ImageNotFoundException, IOException {
        candidateImageRepository.save(CandidateImage.builder().name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(file.getBytes()).build());
        if (file != null) {
            return "Immage Upload SucessFully";
        }
        throw new ImageNotFoundException("x");
    }

    @Override
    public byte[] getImmage(String fileName) {
        Optional<CandidateImage> image = candidateImageRepository.findByName(fileName);
        byte[] returnImg = image.map(CandidateImage::getImageData).orElse(null);
        return returnImg;
    }

    @Override
    public byte[] getImageById(Long id) {
        Optional<CandidateImage> image = candidateImageRepository.findById(id);
        return image.map(CandidateImage::getImageData).orElse(null);
    }

    @Override
    public List<CandidateImage> getAllImmage() {

        return  candidateImageRepository.findAll();
    }


    public void deleteFileById(Long id) {
        candidateImageRepository.deleteById(id);
    }
}

