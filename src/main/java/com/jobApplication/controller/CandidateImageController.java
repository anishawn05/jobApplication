package com.jobApplication.controller;


import com.jobApplication.Exception.ImageNotFoundException;
import com.jobApplication.Exception.InValidJobIdException;

import com.jobApplication.model.CandidateImage;
import com.jobApplication.service.CandidateImageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/management")
@Tag(name = "Files")
public class CandidateImageController {
    @Autowired
    CandidateImageServiceImpl candidateImageServiceImpl;

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException, ImageNotFoundException {
        String img = candidateImageServiceImpl.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK).body(img);
    }
@Operation(
        description = "get endpoint for manager",
        summary = "this is a summary for management get endpoint",
        responses = {
                @ApiResponse(
                        description = "success",
                        responseCode = "200"
                ) ,
                @ApiResponse(
                        description = "UnAuthorize/Invalid Token",
                        responseCode = "403"
                )
        }
)
    @GetMapping("/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String fileName) {
        byte[] fileData = candidateImageServiceImpl.getImmage(fileName);

        if (fileData != null) {
            MediaType mediaType = MediaType.IMAGE_JPEG; // Default to image type

            // Adjust content type based on file extension
            if (fileName.endsWith(".pdf")) {
                mediaType = MediaType.APPLICATION_PDF;
            } // Add more conditions for other file types

            if (fileName.endsWith(".gif")) {
                mediaType = MediaType.IMAGE_GIF;//this will use show gif
                //MediaType.APPLICATION_OCTET_STREAM this will  used to download any type documents or img
            }
            if (fileName.endsWith(".mp4") || fileName.endsWith(".3gp")) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(fileData);
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        candidateImageServiceImpl.deleteFileById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getImageById/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) throws InValidJobIdException {
        byte[] imageData = candidateImageServiceImpl.getImageById(id);
        if (imageData != null) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(imageData);
        } else {
            throw new InValidJobIdException("image not found at the momment");
        }
    }

    @GetMapping("/getAllImage")
    public List<CandidateImage> getAllImage() {
        List<CandidateImage> img = candidateImageServiceImpl.getAllImmage();
        return img;
    }
}
