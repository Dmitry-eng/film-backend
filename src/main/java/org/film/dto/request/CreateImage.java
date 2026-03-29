package org.film.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateImage {

    private Boolean isPrimary;

    @NotBlank
    @Schema(example = "MQ==")
    private String image;

}
