package org.film.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ServiceException extends RuntimeException {

    private final ServiceExceptionType type;

}
