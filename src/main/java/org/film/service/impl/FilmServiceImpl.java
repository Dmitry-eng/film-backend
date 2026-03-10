package org.film.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.film.dto.request.CreateFilm;
import org.film.dto.request.UpdateFilm;
import org.film.dto.request.filter.FilterContext;
import org.film.dto.response.FullFilmInfo;
import org.film.dto.response.ShortFilmInfo;
import org.film.entity.FilmEntity;
import org.film.execption.ServiceException;
import org.film.mapper.FilmMapper;
import org.film.repository.FilmRepository;
import org.film.service.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.film.execption.ServiceExceptionType.FILM_NOT_FOUND;
import static org.film.util.PageableHelper.createPageable;
import static org.film.util.SpecificationHelper.createSpecification;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmMapper filmMapper;
    private final FilmRepository filmRepository;

    @Override
    @Transactional
    public Set<ShortFilmInfo> getAllFilms(FilterContext context, Boolean ignoreRemove) {
        Pageable pageable = createPageable(context);
        Specification<FilmEntity> specification = createSpecification(context, ignoreRemove);

        List<FilmEntity> filmEntities = null;
        if (pageable != null) {
            filmEntities = filmRepository.findAll(specification, pageable)
                    .getContent();
        } else {
            filmEntities = filmRepository.findAll(specification);
        }

        return Optional.of(filmEntities)
                .orElse(List.of())
                .stream()
                .map(filmMapper::toShortFilmInfo)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteFilmById(Long id) {
        FilmEntity filmEntity = filmRepository.findById(id)
                .orElseThrow(() -> new ServiceException(FILM_NOT_FOUND));

        filmEntity.setIsDeleted(true);

        filmRepository.save(filmEntity);
    }

    @Override
    @Transactional
    public FullFilmInfo getFilmById(Long id, Boolean ignoreRemove) {
        FilmEntity film = filmRepository.findById(id)
                .filter(filmEntity -> Boolean.TRUE.equals(ignoreRemove) || !Boolean.FALSE.equals(filmEntity.getIsDeleted()))
                .orElseThrow(() -> new ServiceException(FILM_NOT_FOUND));

        return filmMapper.mapFullFilmInfo(film);
    }

    @Override
    public FullFilmInfo createFilm(CreateFilm film) {
        FilmEntity filmEntity = filmMapper.createFilm(film);
        FilmEntity savedFilmEntity = filmRepository.save(filmEntity);
        return filmMapper.mapFullFilmInfo(savedFilmEntity);
    }

    @Override
    @Transactional
    public FullFilmInfo updateFilm(UpdateFilm film) {
        FilmEntity entity = filmRepository.findById(film.getId())
                .orElseThrow(() -> new ServiceException(FILM_NOT_FOUND));
        filmMapper.updateFilm(entity, film);
        FilmEntity updatedEntity = filmRepository.save(entity);
        return filmMapper.mapFullFilmInfo(updatedEntity);
    }

    private void validateRequest(Object object) {
//     TODO  после реализации авторизации реализовать проверку запроса
    }
}
