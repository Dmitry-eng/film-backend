package org.film.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateImage {

    private Boolean isPrimary;

    @NotBlank
    private String image;

}
