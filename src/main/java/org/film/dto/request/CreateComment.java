package org.film.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateComment {

    @NotNull
    private Long filmId;

    private Long previewCommentId;

    @NotBlank
    @Schema(example = "Норм")
    private String value;

}
