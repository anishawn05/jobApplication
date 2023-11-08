package com.jobApplication.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class InValidJobIdException extends Exception{
    public InValidJobIdException(String message){
        super(message);
    }

}
