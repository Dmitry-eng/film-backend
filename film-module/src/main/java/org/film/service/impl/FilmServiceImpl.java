package org.film.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.film.db.entity.AbstractEntity;
import org.film.db.entity.FilmEntity;
import org.film.db.entity.ImageFilmEntity;
import org.film.db.repository.FilmRepository;
import org.film.db.repository.ImageRepository;
import org.film.dto.request.CreateFilm;
import org.film.dto.request.UpdateFilm;
import org.film.dto.request.UpdateImage;
import org.film.dto.request.filter.FilterContext;
import org.film.dto.response.FullFilmInfo;
import org.film.dto.response.Image;
import org.film.dto.response.ShortFilmInfo;
import org.film.exception.ServiceException;
import org.film.mapper.FilmMapper;
import org.film.service.FilmService;
import org.film.service.StorageService;
import org.mapstruct.Named;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.film.exception.ServiceExceptionType.*;
import static org.film.util.ConstantUtils.IMAGES_BUCKET_NAME;
import static org.film.util.PageableHelper.createPageable;
import static org.film.util.SpecificationHelper.createSpecification;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmMapper filmMapper;
    private final FilmRepository filmRepository;
    private final SecurityContextService securityContextService;
    private final StorageService storageService;
    private final ImageRepository imageRepository;

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
                .map(entity -> {
                    ShortFilmInfo shortFilmInfo = filmMapper.toShortFilmInfo(entity);
                    ImageFilmEntity image = getPrimaryImage(entity);
                    String data = storageService.downloadFile(IMAGES_BUCKET_NAME, image.getId().toString());
                    shortFilmInfo.setImage(data);
                    return shortFilmInfo;
                })
                .collect(Collectors.toSet());
    }


    protected ImageFilmEntity getPrimaryImage(FilmEntity filmEntity) {
        return Optional.of(filmEntity)
                .map(FilmEntity::getImages)
                .orElse(List.of())
                .stream()
                .filter(imageFilmEntity -> !Boolean.TRUE.equals(imageFilmEntity.getIsDeleted()))
                .filter(ImageFilmEntity::getIsPrimary)
                .findAny().orElse(null);
    }

    @Override
    public void deleteFilmById(Long id) {

        if (!securityContextService.isAdmin()) {
            throw new ServiceException(ACCESS_DENIED);
        }

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
        FullFilmInfo fullFilmInfo = filmMapper.mapFullFilmInfo(film);

        List<Image> images = Optional.of(film)
                .map(FilmEntity::getImages)
                .orElse(List.of())
                .stream()
                .map(imageFilmEntity -> {
                    try {
                        String data = storageService.downloadFile(IMAGES_BUCKET_NAME, imageFilmEntity.getId().toString());
                        return filmMapper.mapImage(data, imageFilmEntity);
                    } catch (Exception ex) {
                        log.error("Error build file", ex);
                        return null;
                    }

                }).toList();

        fullFilmInfo.setImages(images);

        return fullFilmInfo;
    }

    @Override
    @Transactional
    public FullFilmInfo createFilm(CreateFilm film) {
        if (!securityContextService.isAdmin()) {
            throw new ServiceException(ACCESS_DENIED);
        }
        FilmEntity filmEntity = filmMapper.createFilm(film);

        Optional.of(film)
                .map(CreateFilm::getImages)
                .orElse(List.of())
                .forEach(image -> {
                    ImageFilmEntity imageFilmEntity = filmMapper.createImageEntity(image);
                    imageFilmEntity = imageRepository.save(imageFilmEntity);
                    storageService.uploadFile(IMAGES_BUCKET_NAME, imageFilmEntity.getId().toString(), image.getImage());
                    if (filmEntity.getImages() == null) {
                        filmEntity.setImages(new ArrayList<>());
                    }
                    filmEntity.getImages().add(imageFilmEntity);
                });

        FilmEntity savedFilmEntity = filmRepository.save(filmEntity);
        return filmMapper.mapFullFilmInfo(savedFilmEntity);
    }

    @Override
    @Transactional
    public FullFilmInfo updateFilm(UpdateFilm film) {
        if (!securityContextService.isAdmin()) {
            throw new ServiceException(ACCESS_DENIED);
        }
        FilmEntity entity = filmRepository.findById(film.getId())
                .orElseThrow(() -> new ServiceException(FILM_NOT_FOUND));
        filmMapper.updateFilm(entity, film);

        List<Long> incomingIds = Optional.of(film)
                .map(UpdateFilm::getImages)
                .orElse(List.of())
                .stream()
                .map(UpdateImage::getId)
                .filter(Objects::nonNull)
                .toList();

        List<ImageFilmEntity> toDelete = entity.getImages().stream()
                .filter(img -> !incomingIds.contains(img.getId()))
                .toList();

        toDelete
                .stream().map(AbstractEntity::getId)
                .forEach(aLong -> {
                    try {
                        storageService.removeFile(IMAGES_BUCKET_NAME, aLong.toString());
                    } catch (Exception e) {
                        log.error("Ignore exception for remove file, bucket: {}, name: {}", IMAGES_BUCKET_NAME, aLong, e);
                    }
                });

        Optional.of(film).map(UpdateFilm::getImages).orElse(List.of()).stream()
                .forEach(img -> {
                    ImageFilmEntity imageFilmEntity = null;
                    if (img == null) {
                        imageFilmEntity = filmMapper.map(img);
                        imageFilmEntity = imageRepository.save(imageFilmEntity);
                        storageService.uploadFile(IMAGES_BUCKET_NAME, entity.getId().toString(), img.getImage());
                    } else {
                        imageFilmEntity = imageRepository.findById(img.getId())
                                .orElseThrow(() -> new ServiceException(IMAGE_NOT_FOUND));

                        Optional.ofNullable(img.getIsPrimary())
                                .ifPresent(imageFilmEntity::setIsPrimary);
                    }

                    if (entity.getImages() == null) {
                        entity.setImages(new ArrayList<>());
                    }
                    entity.getImages().add(imageFilmEntity);
                });

        FilmEntity updatedEntity = filmRepository.save(entity);
        return filmMapper.mapFullFilmInfo(updatedEntity);
    }


    private void validateRequest(Object object) {
//     TODO  после реализации авторизации реализовать проверку запроса
    }
}
