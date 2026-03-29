package org.film.dto.request.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FilterOperator {
    EQUALS("="),
    NOT_EQUALS("!="),
    CONTAINS("LIKE"),
    IN("IN");

    @Getter
    private final String symbol;

}