package org.film.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateFilm {

    @NotNull
    private Long id;

    private List<@Valid UpdateImage> images;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @AssertTrue(message = "At least one image must be set as primary")
    public boolean validateImages() {
        if (images == null) {
            return true;
        }

        return images
                .stream()
                .anyMatch(UpdateImage::getIsPrimary);
    }

}
