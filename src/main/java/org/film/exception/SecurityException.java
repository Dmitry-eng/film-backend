package org.film.exception;


import lombok.RequiredArgsConstructor;
import org.film.exception.type.SecurityExceptionType;

@RequiredArgsConstructor
public class SecurityException extends RuntimeException {

    private final SecurityExceptionType type;

    @Override
    public String getMessage() {
        return type.getValue();
    }

}
