package com.jobApplication.audit;

import com.jobApplication.dao.JobRepository;
import com.jobApplication.model.Job;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Aspect
public class AuditingAspect {
    private final AuditLogRepository auditLogRepository;
    private final JobRepository jobRepository;

    @Around("@annotation(auditable)")
    public Object logChanges(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();

        Job updatedJob = (Job) args[0];
        Long id = (Long) args[1];

        Job oldJob = jobRepository.findById(id).orElse(null);
        if (oldJob != null) {
            // Set the correct ID for updatedJob before saving to the audit log
            updatedJob.setId(id);

            AuditLog auditLog = new AuditLog();
            auditLog.setEntityType("Job");
            auditLog.setEntityId(id);
            auditLog.setOperation("UPDATE");
            auditLog.setOldValue(oldJob.toString());
            auditLog.setNewValue(updatedJob.toString());  // Now with the correct ID
            auditLog.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(auditLog);
        }
        return result;
    }

}

