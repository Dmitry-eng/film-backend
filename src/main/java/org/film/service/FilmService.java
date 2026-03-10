package org.film.service;

import jakarta.validation.Valid;
import org.film.dto.request.CreateFilm;
import org.film.dto.request.UpdateFilm;
import org.film.dto.request.filter.FilterContext;
import org.film.dto.response.FullFilmInfo;
import org.film.dto.response.ShortFilmInfo;

import java.util.Set;

public interface FilmService {

    Set<ShortFilmInfo> getAllFilms(FilterContext context, Boolean ignoreRemove);

    void deleteFilmById(Long id);

    FullFilmInfo getFilmById(Long id, Boolean ignoreRemove);

    FullFilmInfo createFilm(CreateFilm film);

    FullFilmInfo updateFilm(@Valid UpdateFilm film);
}
