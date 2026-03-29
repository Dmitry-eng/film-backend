package org.film.dto.request.filter;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class FilterContext {

    private List<FilterCriteria> filter;

    @Valid
    private PageRequestDto pageable;

}
