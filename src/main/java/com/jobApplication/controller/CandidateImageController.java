package com.jobApplication.controller;


import com.jobApplication.Exception.ImageNotFoundException;
import com.jobApplication.Exception.InValidJobIdException;
import com.jobApplication.messages.PopupMessage;
import com.jobApplication.model.CandidateImage;
import com.jobApplication.service.CandidateImageServiceImpl;
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
@RequestMapping("/jobApplication/images")
public class CandidateImageController {
    @Autowired
    CandidateImageServiceImpl candidateImageServiceImpl;

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException, ImageNotFoundException {
        String img = candidateImageServiceImpl.uploadImage(file);
        PopupMessage msg = new PopupMessage("immage upload is sucessfully uploaded");
        return ResponseEntity.status(HttpStatus.OK).body(img);
    }

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

        // Add the PopupMessage object to the FlashMap object.
        redirectAttributes.addFlashAttribute("popupMessage", new PopupMessage("Image was Deleted"));

        // Return a 204 No Content response.
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
