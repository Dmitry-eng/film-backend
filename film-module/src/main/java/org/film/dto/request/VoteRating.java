package org.film.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRating {

    @NotNull
    private Long filmId;

    @NotNull
    @Max(10)
    @Min(0)
    private Integer grade;

}
