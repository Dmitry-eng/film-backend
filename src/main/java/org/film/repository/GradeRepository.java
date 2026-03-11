package org.film.repository;

import org.film.entity.AccountEntity;
import org.film.entity.FilmEntity;
import org.film.entity.GradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<GradeEntity, Long> {

    Optional<GradeEntity> getGradeEntityByFilmAndAccount(FilmEntity film, AccountEntity account);

}
