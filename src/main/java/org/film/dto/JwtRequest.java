package org.film.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtRequest {

    @NotBlank
    @Schema(example = "test@test.com")
    private String email;

    @NotBlank
    @Schema(example = "test")
    private String password;

}