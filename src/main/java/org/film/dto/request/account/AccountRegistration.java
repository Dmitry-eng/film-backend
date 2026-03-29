package org.film.dto.request.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountRegistration {

    @NotBlank
    @Schema(example = "Дмитрий")
    private String name;

    @NotBlank
    @Email
    @Schema(example = "test@test.com")
    private String email;

    @NotBlank
    @Schema(example = "123")
    private String password;
}
