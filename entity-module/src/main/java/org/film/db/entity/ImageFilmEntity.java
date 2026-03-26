package org.film.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "filmEntity")
@Table
public class ImageFilmEntity extends AbstractEntity {

    private Boolean isPrimary;

    @ManyToOne
    private FilmEntity filmEntity;
}
