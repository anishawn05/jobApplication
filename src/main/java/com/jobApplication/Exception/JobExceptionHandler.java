package com.jobApplication.Exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JobExceptionHandler {

    JobApplicationException abc;
    @ExceptionHandler(value = JobNotFoundException.class)
    public ResponseEntity<Object>  Handler (JobNotFoundException e){
         abc = new JobApplicationException(e.getMessage());
        return  new ResponseEntity<>(abc, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = InValidJobIdException.class)
    public Object InValidJobIdExceptionHandler(InValidJobIdException e){
         abc = new JobApplicationException(e.getMessage());
        return new ResponseEntity<>(abc,HttpStatus.valueOf(418));
    }
    @ExceptionHandler(UpdationNotFound.class)
    public Object UpdationNotFoundHandler(UpdationNotFound e){
         abc = new JobApplicationException(e.getMessage());
        return new ResponseEntity<>(abc,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ImageNotFoundException.class)
    public JobApplicationException imageNotFoundHanddler (ImageNotFoundException e){
        JobApplicationException abc = new JobApplicationException(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(abc).getBody();
    }
}
