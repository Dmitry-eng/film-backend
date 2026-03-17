package org.film.db.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class GradeEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private FilmEntity film;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @ManyToOne
    private AccountEntity account;

}
