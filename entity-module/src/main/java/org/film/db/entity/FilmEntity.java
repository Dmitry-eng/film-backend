package org.film.db.entity;


import jakarta.persistence.*;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "film_entity_id")
    private List<ImageFilmEntity> images;

    @OneToOne
    @JoinColumn(name = "rating_id")
    private RatingEntity rating;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

}
