package org.film.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateFilm {

    private List<@Valid CreateImage> images;

    @NotBlank
    @Schema(example = "Фильм")
    private String name;

    @NotBlank
    @Schema(example = "Описание")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "Дата выхода")
    private LocalDate releaseDate;

    @Schema(hidden = true)
    @AssertTrue(message = "At least one image must be set as primary")
    public boolean isValidImages() {
        if (images == null) {
            return true;
        }

        return images
                .stream()
                .anyMatch(CreateImage::getIsPrimary);
    }


}
