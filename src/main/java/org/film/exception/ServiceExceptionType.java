package org.film.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ServiceExceptionType {

    COMMENT_NOT_FOUND("Comment not found", HttpStatus.NOT_FOUND),
    FILM_NOT_FOUND("Film not found", HttpStatus.NOT_FOUND),
    EMAIL_EXISTS("Email exists", HttpStatus.CONFLICT),
    IMAGE_NOT_FOUND("Image not found", HttpStatus.NOT_FOUND),
    COMMENT_FILM_MISMATCH("Film ID mismatch between comment and parent comment", HttpStatus.CONFLICT),
    ACCESS_DENIED("Access denied", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;

    private final HttpStatus httpStatus;

}
