package org.film.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateImage {

    private Long id;

    private Boolean isPrimary;

    @NotBlank
    private String image;

}
