package org.film.exception.handler;

import org.apache.commons.lang3.StringUtils;
import org.film.exception.ErrorResponse;
import org.film.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandler {

    private static final String COMMA = ", ";
    private static final String CHAR = "'";


    @org.springframework.web.bind.annotation.ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<?> serviceException(ServiceException ex) {
        HttpStatus status = ex.getType().getHttpStatus();
        ErrorResponse errorResponse = buildResponse(ex.getType().getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = Arrays.stream(Objects.requireNonNull(ex.getDetailMessageArguments()))
                .filter(o -> o instanceof ArrayList<?>)
                .map(o -> (ArrayList<?>) o)
                .flatMap(Collection::stream)
                .map(Object::toString)
                .map(s -> s.replace(CHAR, StringUtils.EMPTY))
                .collect(Collectors.joining(COMMA));

        return buildResponse(errorMessage);
    }

    private ErrorResponse buildResponse(String name) {
        return new ErrorResponse(name, LocalDateTime.now());
    }
}