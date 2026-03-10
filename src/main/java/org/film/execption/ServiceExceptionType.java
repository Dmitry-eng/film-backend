package org.film.execption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ServiceExceptionType {

    COMMENT_NOT_FOUND("Comment not found", HttpStatus.NOT_FOUND),
    FILM_NOT_FOUND("Film not found", HttpStatus.NOT_FOUND),
    COMMENT_FILM_MISMATCH("Film ID mismatch between comment and parent comment", HttpStatus.CONFLICT),
    ACCESS_DENIED("Access denied", HttpStatus.FORBIDDEN);

    private final String message;

    private final HttpStatus httpStatus;

}
