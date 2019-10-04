package com.asset.jupiter.Util.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ControllerErrorHandler {


    // for jupiter Exception
    @ExceptionHandler(JupiterException.class)
    public final ResponseEntity<ErrorDetails> handleJupiterException(JupiterException ex) {


        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), ex.getErrCode());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    // general
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
