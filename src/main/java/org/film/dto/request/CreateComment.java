package org.film.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateComment {

    @NotNull
    private Long filmId;

    private Long previewCommentId;

    @NotBlank
    private String value;

}
