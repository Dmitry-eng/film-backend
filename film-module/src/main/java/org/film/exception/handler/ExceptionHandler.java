package org.film.exception.handler;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.film.exception.ErrorResponse;
import org.film.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Log4j2
public class ExceptionHandler {

    private static final String COMMA = ", ";


    @org.springframework.web.bind.annotation.ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<?> serviceException(ServiceException ex) {
        log.error("ServiceException: {}", ex.getMessage(), ex);
        HttpStatus status = ex.getType().getHttpStatus();
        ErrorResponse errorResponse = buildResponse(ex.getType().getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage(), ex);
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining("; "))
                ))
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(COMMA));

        return buildResponse(errorMessage);
    }

    private ErrorResponse buildResponse(String name) {
        return new ErrorResponse(name, LocalDateTime.now());
    }
}