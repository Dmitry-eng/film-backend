package org.film.dto.request.filter;

import lombok.Data;

@Data
public class FilterCriteria {

    private String field;

    private FilterOperator operator;

    private String value;

}
