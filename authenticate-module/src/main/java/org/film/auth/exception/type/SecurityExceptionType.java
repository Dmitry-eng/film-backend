package org.film.auth.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SecurityExceptionType {
    ACCOUNT_NOT_FOUND("Account not found"),
    INCORRECT_PASSWORD("Incorrect password"),
    INVALID_JWT_TOKEN("Invalid jwt token");

    private final String value;
}
