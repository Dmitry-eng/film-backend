package org.film.db.repository;

import org.film.db.entity.ImageFilmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageFilmEntity, Long> {
}
