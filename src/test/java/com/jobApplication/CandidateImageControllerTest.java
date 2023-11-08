package com.jobApplication;

import com.jobApplication.Exception.ImageNotFoundException;
import com.jobApplication.controller.CandidateImageController;
import com.jobApplication.serviceimpl.CandidateImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CandidateImageControllerTest {
    @Mock
    CandidateImageService candidateImageService;
    @InjectMocks
    CandidateImageController candidateImageController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(candidateImageController).build();
    }

    @Test
    public void testUploadImage() throws Exception {
        // Create a mock MultipartFile
        MockMultipartFile file = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "image data".getBytes());

        // Mock the behavior of candidateImageService.uploadImage to return a success message
        when(candidateImageService.uploadImage(file)).thenReturn("Image Upload Successfully");

        // Perform the uploadImage request
        mockMvc.perform(
                        multipart("/uploadImage")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Image Upload Successfully"));

        // Verify that candidateImageService.uploadImage was called with the provided MultipartFile
        verify(candidateImageService, times(1)).uploadImage(file);
        verifyNoMoreInteractions(candidateImageService);
    }

}
