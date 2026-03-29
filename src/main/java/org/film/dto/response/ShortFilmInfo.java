package org.film.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ShortFilmInfo {

    private Long id;

    private String image;

    private String name;

    private String description;

    private Double rating;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateDatetime;

}
