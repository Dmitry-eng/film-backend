package org.film.db.repository;

import org.film.db.entity.AccountEntity;
import org.film.db.entity.FilmEntity;
import org.film.db.entity.GradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<GradeEntity, Long> {

    Optional<GradeEntity> getGradeEntityByFilmAndAccount(FilmEntity film, AccountEntity account);

}
