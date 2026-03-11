package org.film.repository;

import org.film.entity.FilmEntity;
import org.film.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

    Optional<RatingEntity> getRatingEntityByFilm(FilmEntity film);

    List<RatingEntity> getEntityByIsRequiredCalculate(Boolean value);
}
