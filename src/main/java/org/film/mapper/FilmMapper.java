package org.film.mapper;

import org.film.dto.request.CreateFilm;
import org.film.dto.request.CreateImage;
import org.film.dto.request.UpdateFilm;
import org.film.dto.request.UpdateImage;
import org.film.dto.response.CommentInfo;
import org.film.dto.response.FullFilmInfo;
import org.film.dto.response.Image;
import org.film.dto.response.ShortFilmInfo;
import org.film.entity.CommentEntity;
import org.film.entity.FilmEntity;
import org.film.entity.ImageFilmEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class FilmMapper {

    private static final String COMMENT_DELETED = "Комментарий удален.";

    public abstract void updateFilm(@MappingTarget FilmEntity entity, UpdateFilm updateFilm);

    @Mapping(target = "fileUri", source = "image")
    @Mapping(target = "id", source = "id")
    public abstract ImageFilmEntity map(UpdateImage image);

    public abstract FilmEntity createFilm(CreateFilm film);

    @Mapping(target = "image", source = "entity", qualifiedByName = "getPrimaryImage")
    public abstract ShortFilmInfo toShortFilmInfo(FilmEntity entity);

    public abstract FullFilmInfo mapFullFilmInfo(FilmEntity entity);

    @Mapping(target = "fileUri", source = "image")
    public abstract ImageFilmEntity map(CreateImage image);

    @Mapping(target = "value", source = "entity", qualifiedByName = "getCommentValue")
    @Mapping(target = "previewCommentId", source = "entity.previewComment.id")
    protected abstract CommentInfo map(CommentEntity entity);

    @Named("getCommentValue")
    protected String getCommentValue(CommentEntity entity) {
        return Boolean.TRUE.equals(entity.getIsDeleted()) ? COMMENT_DELETED : entity.getValue();
    }

    @Mapping(target = "isPrimary", source = "isPrimary", defaultValue = "false")
    @Mapping(target = "image", source = "fileUri")
    protected abstract Image map(ImageFilmEntity image);

    @Named("getPrimaryImage")
    protected String getPrimaryImage(FilmEntity filmEntity) {
        return Optional.of(filmEntity)
                .map(FilmEntity::getImages)
                .orElse(List.of())
                .stream()
                .filter(imageFilmEntity -> !imageFilmEntity.getIsDeleted())
                .filter(ImageFilmEntity::getIsPrimary)
                .map(ImageFilmEntity::getFileUri)
                .findAny().orElse(null);
    }

}
