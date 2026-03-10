package org.film.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FullFilmInfo {

    private Long id;

    private List<Image> images;

    private String name;

    private String description;

    private Double rating;

    private List<CommentInfo> comments;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateDatetime;

}
