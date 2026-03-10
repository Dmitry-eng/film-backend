package org.film.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(exclude = {"images", "comments"})
@Table
@Entity
public class FilmEntity extends AbstractEntity {

    private String name;

    private String description;

    private LocalDate releaseDate;

    @OneToMany(mappedBy = "filmEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageFilmEntity> images;

    private Double rating;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

}
