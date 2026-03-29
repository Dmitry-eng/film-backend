package org.film.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshJwtRequest {

    @NotBlank
    public String refreshToken;

}