package com.jobApplication;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobApplication.Exception.InValidJobIdException;
import com.jobApplication.Exception.JobNotFoundException;
import com.jobApplication.Exception.UpdationNotFound;
import com.jobApplication.controller.JobController;
import com.jobApplication.model.Job;
import com.jobApplication.service.JobService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class JobControllerTest {


    private MockMvc mockMvc;


    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
    }

    @Test
    public void testGetAllJobs() throws Exception {
        List<Job> jobs = Arrays.asList(
                new Job(1L, "Software Engineer", "Develop software", "50000", "80000", "India", null),
                new Job(2L, "Data Scientist", "Analyze data", "60000", "90000", "USA", null)
        );

        when(jobService.getAll()).thenReturn(jobs);

        mockMvc.perform(get("/getAllJob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))  // Change to .value(1L.intValue())
                .andExpect(jsonPath("$[0].title").value("Software Engineer"))
                .andExpect(jsonPath("$[1].id").value(2))  // Change to .value(2L.intValue())
                .andExpect(jsonPath("$[1].title").value("Data Scientist"));

        verify(jobService, times(1)).getAll();
        verifyNoMoreInteractions(jobService);
    }


    @Test
    public void testSorting() throws Exception {
        String field = "minSalary";
        List<Job> abc = Arrays.asList(
                new Job(1l, "Mech", "Designing", "50000", "75000", "guindy", null),
                new Job(2l, "Civil", "Planing", "20000", "100000", "chennai", null));

        when(jobService.getAllBySorting(field)).thenReturn(abc);
        mockMvc.perform(get("/{field}", field))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Mech"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].minSalary").value("20000"));
        verify(jobService, times(1)).getAllBySorting(field);
        verifyNoMoreInteractions(jobService);

    }

    @Test
    public void testPagination() throws Exception {
        int offSet = 0;
        int pageSize = 5;
        List<Job> abc = Arrays.asList(
                new Job(1l, "Mech", "Designing", "50000", "75000", "guindy", null),
                new Job(2l, "Civil", "Planing", "20000", "100000", "chennai", null));
        Page<Job> aaa = new PageImpl<>(abc, PageRequest.of(offSet, pageSize), abc.size());
        when(jobService.paginationJob(offSet, pageSize)).thenReturn(aaa);
        mockMvc.perform(get("/paging/{offset}/{pageSize}", offSet, pageSize))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].minSalary").value("50000"))
                .andExpect(jsonPath("$.content[1].description").value("Planing"))
                .andExpect(jsonPath("$.content[1].location").value("chennai"));
        verify(jobService, times(1)).paginationJob(offSet, pageSize);
        verifyNoMoreInteractions(jobService);

    }

    @Test
    public void testPaginationWithSorting() throws Exception {
        int offset = 0;
        int pageSize = 5;
        String field = "title";

        List<Job> jobs = Arrays.asList(
                new Job(1L, "Software Engineer", "Develop software", "50000", "80000", "India", null),
                new Job(2L, "Data Scientist", "Analyze data", "60000", "90000", "USA", null)
                // Add more jobs as needed
        );

        Page<Job> abc = new PageImpl<>(jobs, PageRequest.of(offset, pageSize), jobs.size());

        when(jobService.paginationJobWithSorting(offset, pageSize, field)).thenReturn(abc);
        mockMvc.perform(get("/paginationWithSorting/{offset}/{pageSize}/{field}", offset, pageSize, field))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Software Engineer"))
                .andExpect(jsonPath("$.content[1].minSalary").value("60000"))
                .andExpect(jsonPath("$.content[1].description").value("Analyze data"));
        verify(jobService, times(1)).paginationJobWithSorting(offset, pageSize, field);
        verifyNoMoreInteractions(jobService);


    }


    @SneakyThrows
    @Test
    public void testGetJobById() throws InValidJobIdException {
        long jobId = 2L;
        Job job = new Job(jobId, "Software Engineer", "Develop software", "50000", "80000", "India", null);

        when(jobService.getJobById(jobId)).thenReturn(job);

        mockMvc.perform(get("/getById/{id}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jobId))
                .andExpect(jsonPath("$.title").value("Software Engineer"));

        verify(jobService, times(1)).getJobById(jobId);
        verifyNoMoreInteractions(jobService);
    }


    @SneakyThrows
    @Test
    public void testGetJobByLocationWithExceptionHandling() throws JobNotFoundException {
        // Negative scenario for invalid location
        String invalidLocation = "usa";
        when(jobService.getByLocation(invalidLocation)).thenThrow(new JobNotFoundException("not accepted"));

        mockMvc.perform(get("/getByLocation/{location}", invalidLocation))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Location not accepted"));

        verify(jobService, times(1)).getByLocation(invalidLocation);
        verifyNoMoreInteractions(jobService);

        // Positive scenario for valid location
        String validLocation = "india";
        List<Job> jobs = Arrays.asList(
                new Job(1L, "Software Engineer", "Develop software", "50000", "80000", validLocation, null),
                new Job(2L, "Data Scientist", "Analyze data", "60000", "90000", validLocation, null)
        );

        when(jobService.getByLocation(validLocation)).thenReturn(jobs);

        MvcResult result = mockMvc.perform(get("/getByLocation/{location}", validLocation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value(validLocation))
                .andExpect(jsonPath("$[0].title").value("Software Engineer"))
                .andReturn();

        verify(jobService, times(1)).getByLocation(validLocation);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void testCreateJob() throws Exception {
        // Create a mock job
        Job job = new Job(1L, "Software Engineer", "Develop software", "50000", "80000", "India", null);

        // Mock the job service to return the created job
        when(jobService.create(job)).thenReturn(job);

        // Create the request body
        String requestBody = new ObjectMapper().writeValueAsString(job);

        // Perform the POST request
        MvcResult result = mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        // Verify the response status
        assertThat(result.getResponse().getStatus()).isEqualTo(200);

        // Verify the response body
        assertThat(result.getResponse().getContentAsString()).isEqualTo(requestBody);

        // Verify that the job service create method was called
        verify(jobService).create(job);
    }

    @SneakyThrows
    @Test
    public void testUpdateJobSuccess() {
        Long jobId = 1L;
        Job updatedJob = new Job();
        updatedJob.setId(jobId);
        updatedJob.setTitle("Updated Job Title");
        updatedJob.setDescription("Updated Job Description");
        updatedJob.setMinSalary("60000");
        updatedJob.setMaxSalary("90000");
        updatedJob.setLocation("USA");
        updatedJob.setCompany(null);


        // Mock the behavior of jobService.update to return the updated job
        when(jobService.update(any(Job.class), eq(jobId))).thenReturn(updatedJob);

        String requestBody = new ObjectMapper().writeValueAsString(updatedJob);

        mockMvc.perform(put("/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedJob.getId()))
                .andExpect(jsonPath("$.title").value(updatedJob.getTitle()));

        // Verify that the jobService's update method was called with the provided Job object and job ID
        verify(jobService, times(1)).update(any(Job.class), eq(1L));
        verifyNoMoreInteractions(jobService);
    }


    @Test
    public void testUpdateJobWithUpdateFailure() throws UpdationNotFound, Exception {
        // Create a new Job object to be updated
        Job job = new Job(105L, "Title", "Description", "60000", "90000", "USA", null);

        // Mock the update() method of the JobService class to throw an UpdationNotFound exception
        Mockito.when(jobService.update(eq(job), eq(105L))).thenThrow(new UpdationNotFound("you can't update"));

        // Call the update() method of the JobController class, passing in the updated Job object
        MvcResult result = mockMvc.perform(put("/update/{id}", 105L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(job))
                )
                .andReturn();

        // Verify that the update() method returns a 400 Bad Request HTTP status code
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // Verify that the JobService.update() method is called
        Mockito.verify(jobService, times(1)).update(any(Job.class), anyLong());
    }

    @Test
    public void testDeleteJob() throws Exception {
        long jobId = 1L;

        // Mocking the service to return a job when delete is called
        Job job = new Job(jobId, "Software Engineer", "Develop software", "50000", "80000", "India", null);
        when(jobService.delete(jobId)).thenReturn(String.valueOf(job));// job.toString() or

        // Performing the DELETE request
        mockMvc.perform(delete("/delete/{id}", jobId))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete Success"));

        // Verifying that the service's delete method was called with the correct jobId
        verify(jobService, times(1)).delete(jobId);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void testGetByLocationNativeQuery() throws Exception {
        String location = "chennai";
        List<Job> jobs = Arrays.asList(
                new Job(1L, "Software Engineer", "Develop software", "50000", "80000", location, null),
                new Job(2L, "Data Scientist", "Analyze data", "60000", "90000", location, null)
        );
        when(jobService.getByLocationNativeQuery(location)).thenReturn(jobs);
        mockMvc.perform(get("/getByLocationNativeQuery").queryParam("location", location))
                //.andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Software Engineer"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].minSalary").value("60000"));
        verify(jobService, times(1)).getByLocationNativeQuery(location);
        verifyNoMoreInteractions(jobService);


    }

}

