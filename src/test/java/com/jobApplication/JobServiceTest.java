
package com.jobApplication;

import com.jobApplication.Exception.InValidJobIdException;
import com.jobApplication.Exception.JobNotFoundException;
import com.jobApplication.dao.JobRepository;
import com.jobApplication.model.Job;
import com.jobApplication.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class JobServiceTest {


    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllJob() {
        List<Job> allJob = Arrays.asList(new Job(1L, "Mech", "Design", "50000", "150000", "Chennai",null),
                new Job(2L, "Cse", "Software", "30000", "100000", "Delhi",null));

        when(jobRepository.findAll()).thenReturn(allJob);
        List<Job> result = jobService.getAll();
        assertEquals(2, result.size());
        assertEquals("Design", result.get(0).getDescription());
        assertEquals("100000", result.get(1).getMaxSalary());
        verify(jobRepository, times(1)).findAll();
        verifyNoMoreInteractions(jobRepository);
    }

    @Test
    public void testGetAllJobBySorting() {
        String field = "location";
        List<Job> allJob = Arrays.asList(new Job(1L, "Mech", "Design", "50000", "150000", "Chennai",null),
                new Job(2L, "Cse", "Software", "30000", "100000", "Delhi",null));

        when(jobRepository.findAll(Sort.by(Sort.Direction.ASC, field))).thenReturn(allJob);
        List<Job> result = jobService.getAllBySorting(field);
        assertEquals(2, result.size());
        assertEquals("Design", result.get(0).getDescription());
        assertEquals("100000", result.get(1).getMaxSalary());
        verify(jobRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, field));
        verifyNoMoreInteractions(jobRepository);
    }

    @Test
    public void testPaginationOfJob() {
        int offSet = 0;
        int pageSize = 5;
        List<Job> allJob = Arrays.asList(new Job(1L, "Mech", "Design", "50000", "150000", "Chennai",null),
                new Job(2L, "Cse", "Software", "30000", "100000", "Delhi",null));
        Page<Job> pages = new PageImpl<>(allJob, PageRequest.of(offSet, pageSize), allJob.size());
        when(jobRepository.findAll(PageRequest.of(offSet, pageSize))).thenReturn(pages);
        Page<Job> result = jobService.paginationJob(offSet, pageSize);
        assertEquals(2, result.getContent().size());
        assertEquals("Mech", result.getContent().get(0).getTitle());
        assertEquals("Design", result.getContent().get(0).getDescription());
        assertEquals("30000", result.getContent().get(1).getMinSalary());
        assertEquals("Delhi", result.getContent().get(1).getLocation());
        verify(jobRepository, times(1)).findAll(PageRequest.of(offSet, pageSize));
        verifyNoMoreInteractions(jobRepository);

    }

    @Test
    public void testPaginationWithSorting() {
        int offSet = 0;
        int pageSize = 5;
        String field = "maxSalary";
        List<Job> allJob = Arrays.asList(new Job(1L, "Mech", "Design", "50000", "150000", "Chennai",null),
                new Job(2L, "Cse", "Software", "30000", "100000", "Delhi",null));
        Page<Job> pages = new PageImpl<>(allJob, PageRequest.of(offSet, pageSize), allJob.size());
        when(jobRepository.findAll(PageRequest.of(offSet, pageSize, Sort.by(Sort.Direction.ASC, field)))).thenReturn(pages);
        Page<Job> result = jobService.paginationJobWithSorting(offSet, pageSize, field);
        assertEquals(2, result.getContent().size());
        assertEquals("Mech", result.getContent().get(0).getTitle());
        assertEquals("Design", result.getContent().get(0).getDescription());
        assertEquals(2L, result.getContent().get(1).getId());
        assertEquals("Delhi", result.getContent().get(1).getLocation());
        verify(jobRepository, times(1)).findAll(PageRequest.of(offSet, pageSize, Sort.by(Sort.Direction.ASC, field)));
        verifyNoMoreInteractions(jobRepository);
    }

    @Test
    public void testCreateJob() {
        // Create a sample Job object
        Job job = new Job();
        job.setTitle("Mech");
        job.setDescription("Design");
        job.setMinSalary("50000");
        job.setMaxSalary("150000");
        job.setLocation("Chennai");

        // Mock the behavior of jobRepository.save
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        // Call the create method in the jobService
        Job createdJob = jobService.create(job);

        // Verify that jobRepository.save was called with the correct Job object
        verify(jobRepository, times(1)).save(job);

        // Verify that the returned Job object matches the expected values
        assertEquals("Mech", createdJob.getTitle());
        assertEquals("Design", createdJob.getDescription());
        assertEquals("50000", createdJob.getMinSalary());
        assertEquals("150000", createdJob.getMaxSalary());
        assertEquals("Chennai", createdJob.getLocation());

        // Verify that no more interactions with jobRepository occurred
        verifyNoMoreInteractions(jobRepository);
    }

    @Test
    public void testGetJobById() throws InValidJobIdException {
        Long Id = 2L;

        Job j = new Job(2L, "Cse", "Software", "30000", "100000", "Delhi",null);

        when(jobRepository.findById(Id)).thenReturn(Optional.of(j));
        Job result = jobService.getJobById(Id);
        assertEquals("Cse", result.getTitle());
        assertEquals("30000", result.getMinSalary());
        verify(jobRepository, times(1)).findById(Id);
        verifyNoMoreInteractions(jobRepository);
    }
    @Test
    public void testGetByLocationNativeQuery() throws JobNotFoundException {
        String location = " ";
        List<Job> jobs = Arrays.asList(
                new Job(1L, "Software Engineer", "Develop software", "50000", "80000", location,null),
                new Job(2L, "Data Scientist", "Analyze data", "60000", "90000", location,null)
        );

        // Mock the behavior of jobRepository.getByLocationNativeQuery to return the list of jobs
        when(jobRepository.getByLocationNativeQuery(location)).thenReturn(jobs);

        // Call the getByLocationNativeQuery method in the jobService
        List<Job> result = jobService.getByLocationNativeQuery(location);

        // Verify that jobRepository.getByLocationNativeQuery was called with the provided location
        verify(jobRepository, times(1)).getByLocationNativeQuery(location);

        // Verify that the result list matches the expected list
        assertEquals(jobs, result);

        // Verify that no more interactions with jobRepository occurred
        verifyNoMoreInteractions(jobRepository);

         }

    @Test
    public void testGetByLocationNativeQueryException() {
        String locations = " ";

        // Spy on the jobRepository object
        JobRepository jobRepositorySpy = Mockito.spy(jobRepository);

        // Create a new JobNotFoundException object with the desired message
        JobNotFoundException jobNotFoundException = new JobNotFoundException("job not in there ");

        // Tell the spy to throw the JobNotFoundException object when the getByLocationNativeQuery() method is called
        Mockito.doThrow(jobNotFoundException).when(jobRepositorySpy).getByLocationNativeQuery(locations);

        // Use assertThrows to check if the exception is thrown
        assertThrows(JobNotFoundException.class, () -> jobService.getByLocationNativeQuery(locations));

        // Verify that jobRepositorySpy.getByLocationNativeQuery was called with the provided location
        Mockito.verify(jobRepositorySpy, times(1)).getByLocationNativeQuery(locations);

        // Verify that no more interactions with jobRepositorySpy occurred
        Mockito.verifyNoMoreInteractions(jobRepositorySpy);
    }

}


