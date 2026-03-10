package org.film.dto.request.filter;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageRequestDto {

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;

}
