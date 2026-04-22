package org.medilabo.screening.controller.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class) // Si un autre microservice renvoie not found (404)
    public ProblemDetail handleNotFoundException(FeignException.NotFound ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Patient or Notes not found in remote microservice.");
        return problemDetail;
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class) // Si un microservice est éteint ou indisponible
    public ProblemDetail handleServiceUnavailableException(FeignException.ServiceUnavailable ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        problemDetail.setTitle("A required microservice is currently down.");
        return problemDetail;
    }
}
