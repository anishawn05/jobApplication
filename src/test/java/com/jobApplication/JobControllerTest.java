/*
package com.jobApplication;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        mockMvc.perform(get("/api/v1/admin/getAllJob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Software Engineer"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Data Scientist"));

        verify(jobService, times(1)).getAll();
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void testSorting() throws Exception {
        String field = "minSalary";
        List<Job> jobs = Arrays.asList(
                new Job(1L, "Mech", "Designing", "50000", "75000", "Guindy", null),
                new Job(2L, "Civil", "Planning", "20000", "100000", "Chennai", null)
        );

        when(jobService.getAllBySorting(field)).thenReturn(jobs);

        mockMvc.perform(get("/api/v1/admin/{field}", field))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Mech"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].minSalary").value("20000"));

        verify(jobService, times(1)).getAllBySorting(field);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void testPagination() throws Exception {
        int offset = 0;
        int pageSize = 5;
        List<Job> jobs = Arrays.asList(
                new Job(1L, "Mech", "Designing", "50000", "75000", "Guindy", null),
                new Job(2L, "Civil", "Planning", "20000", "100000", "Chennai", null)
        );
        Page<Job> page = new PageImpl<>(jobs, PageRequest.of(offset, pageSize), jobs.size());

        when(jobService.paginationJob(offset, pageSize)).thenReturn(page);

        mockMvc.perform(get("/api/v1/admin/paging/{offset}/{pageSize}", offset, pageSize))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].minSalary").value("50000"))
                .andExpect(jsonPath("$.content[1].description").value("Planning"))
                .andExpect(jsonPath("$.content[1].location").value("Chennai"));

        verify(jobService, times(1)).paginationJob(offset, pageSize);
        verifyNoMoreInteractions(jobService);
    }

    @SneakyThrows
    @Test
    public void testGetJobById() {
        long jobId = 2L;
        Job job = new Job(jobId, "Software Engineer", "Develop software", "50000", "80000", "India", null);

        when(jobService.getJobById(jobId)).thenReturn(job);

        mockMvc.perform(get("/api/v1/admin/getById/{id}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jobId))
                .andExpect(jsonPath("$.title").value("Software Engineer"));

        verify(jobService, times(1)).getJobById(jobId);
        verifyNoMoreInteractions(jobService);
    }

    @SneakyThrows
    @Test
    public void testGetJobByLocationWithExceptionHandling() {
        // Negative scenario for invalid location
        String invalidLocation = "usa";
        when(jobService.getByLocation(invalidLocation)).thenThrow(new JobNotFoundException("Location not accepted"));

        mockMvc.perform(get("/api/v1/admin/getByLocation/{location}", invalidLocation))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Location not accepted"));

        verify(jobService, times(1)).getByLocation(invalidLocation);
        verifyNoMoreInteractions(jobService);

        // Positive scenario for valid location
        String validLocation = "India";
        List<Job> jobs = Arrays.asList(
                new Job(1L, "Software Engineer", "Develop software", "50000", "80000", validLocation, null),
                new Job(2L, "Data Scientist", "Analyze data", "60000", "90000", validLocation, null)
        );

        when(jobService.getByLocation(validLocation)).thenReturn(jobs);

        mockMvc.perform(get("/api/v1/admin/getByLocation/{location}", validLocation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value(validLocation))
                .andExpect(jsonPath("$[0].title").value("Software Engineer"));

        verify(jobService, times(1)).getByLocation(validLocation);
        verifyNoMoreInteractions(jobService);
    }

    @SneakyThrows
    @Test
    public void testCreateJob() {
        Job job = new Job(1L, "Software Engineer", "Develop software", "50000", "80000", "India", null);

        when(jobService.create(any(Job.class))).thenReturn(job);

        String requestBody = new ObjectMapper().writeValueAsString(job);

        mockMvc.perform(post("/api/v1/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(job.getId()))
                .andExpect(jsonPath("$.title").value(job.getTitle()));

        verify(jobService, times(1)).create(any(Job.class));
        verifyNoMoreInteractions(jobService);
    }

    @SneakyThrows
    @Test
    public void testUpdateJobSuccess() {
        Long jobId = 1L;
        Job updatedJob = new Job(jobId, "Updated Job Title", "Updated Job Description", "60000", "90000", "USA", null);

        when(jobService.update(any(Job.class), eq(jobId))).thenReturn(updatedJob);

        String requestBody = new ObjectMapper().writeValueAsString(updatedJob);

        mockMvc.perform(put("/api/v1/admin/update/{id}", jobId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedJob.getId()))
                .andExpect(jsonPath("$.title").value(updatedJob.getTitle()));

        verify(jobService, times(1)).update(any(Job.class), eq(jobId));
        verifyNoMoreInteractions(jobService);
    }

    @SneakyThrows
    @Test
    public void testUpdateJobWithUpdateFailure() {
        Long jobId = 105L;
        Job job = new Job(jobId, "Title", "Description", "60000", "90000", "USA", null);

        when(jobService.update(any(Job.class), eq(jobId))).thenThrow(new UpdationNotFound("Can't update"));

        String requestBody = new ObjectMapper().writeValueAsString(job);

        mockMvc.perform(put("/api/v1/admin/update/{id}", jobId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Can't update"));

        verify(jobService, times(1)).update(any(Job.class), eq(jobId));
        verifyNoMoreInteractions(jobService);
    }

    @SneakyThrows
    @Test
    public void testDeleteJob() {
        Long jobId = 1L;

        doNothing().when(jobService).deleteById(jobId);

        mockMvc.perform(delete("/api/v1/admin/delete/{id}", jobId))
                .andExpect(status().isNoContent());

        verify(jobService, times(1)).deleteById(jobId);
        verifyNoMoreInteractions(jobService);
    }

    @SneakyThrows
    @Test
    public void testDeleteJobWithException() {
        Long jobId = 1L;

        doThrow(new InValidJobIdException("Job ID not valid")).when(jobService).deleteById(jobId);

        mockMvc.perform(delete("/api/v1/admin/delete/{id}", jobId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Job ID not valid"));

        verify(jobService, times(1)).deleteById(jobId);
        verifyNoMoreInteractions(jobService);
    }
}
*/
