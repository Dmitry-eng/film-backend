package org.film.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.film.dto.request.CreateFilm;
import org.film.dto.request.UpdateFilm;
import org.film.dto.request.filter.FilterContext;
import org.film.dto.response.FullFilmInfo;
import org.film.dto.response.ShortFilmInfo;
import org.film.service.FilmService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/film")
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @Operation(summary = "Получить все фильмы с фильтрацией")
    @PostMapping("/all")
    public Set<ShortFilmInfo> getAllFilms(@Valid @RequestBody(required = false) FilterContext context, @RequestParam(required = false) Boolean ignoreRemove) {
        return filmService.getAllFilms(context, ignoreRemove);
    }

    @Operation(summary = "Удаление фильма")
    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable Long id) {
        filmService.deleteFilmById(id);
    }

    @Operation(summary = "Получить фильм по id")
    @GetMapping("/{id}")
    public FullFilmInfo getFilmById(@PathVariable Long id, @RequestParam(required = false) Boolean ignoreRemove) {
        return filmService.getFilmById(id, ignoreRemove);
    }

    @Operation(summary = "Добавление фильма в каталог")
    @PostMapping
    public FullFilmInfo createFilm(@RequestBody @Valid CreateFilm film) {
        return filmService.createFilm(film);
    }

    @Operation(summary = "Обновление фильма")
    @PutMapping
    public FullFilmInfo updateFilm(@RequestBody @Valid UpdateFilm film) {
        return filmService.updateFilm(film);
    }
}
