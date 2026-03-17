package org.film.auth.exception;


import lombok.RequiredArgsConstructor;
import org.film.auth.exception.type.SecurityExceptionType;

@RequiredArgsConstructor
public class SecurityException extends RuntimeException {

    private final SecurityExceptionType type;

    @Override
    public String getMessage() {
        return type.getValue();
    }

}
