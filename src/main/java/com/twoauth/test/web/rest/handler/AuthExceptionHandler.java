package com.twoauth.test.web.rest.handler;

import com.twoauth.test.exception.UserUnauthenticated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler({UserUnauthenticated.class})
    public final ResponseEntity<String> handleException(UserUnauthenticated ex, WebRequest request) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
