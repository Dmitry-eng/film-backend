package org.film.db.repository;

import org.film.db.entity.FilmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FilmRepository extends JpaRepository<FilmEntity, Long>, JpaSpecificationExecutor<FilmEntity> {



}
