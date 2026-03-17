package org.film.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table
@Data
public class RatingEntity extends AbstractEntity {

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "film_id")
    private FilmEntity film;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<Integer, Long> gradeDistribution = new HashMap<>();

    private Double averageRating;

    private Boolean isRequiredCalculate = false;

}