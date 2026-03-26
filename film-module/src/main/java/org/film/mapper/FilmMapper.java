package org.film.mapper;

import org.film.db.entity.CommentEntity;
import org.film.db.entity.FilmEntity;
import org.film.db.entity.ImageFilmEntity;
import org.film.dto.request.CreateFilm;
import org.film.dto.request.CreateImage;
import org.film.dto.request.UpdateFilm;
import org.film.dto.request.UpdateImage;
import org.film.dto.response.CommentInfo;
import org.film.dto.response.FullFilmInfo;
import org.film.dto.response.Image;
import org.film.dto.response.ShortFilmInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class FilmMapper {

    private static final String COMMENT_DELETED = "Комментарий удален.";

    @Mapping(target = "images", ignore = true)
    public abstract void updateFilm(@MappingTarget FilmEntity entity, UpdateFilm updateFilm);

    @Mapping(target = "id", source = "id")
    public abstract ImageFilmEntity map(UpdateImage image);

    @Mapping(target = "images", ignore = true)
    public abstract FilmEntity createFilm(CreateFilm film);

    @Mapping(target = "image", ignore = true)
    @Mapping(target = "rating", source = "entity.rating.averageRating")
    public abstract ShortFilmInfo toShortFilmInfo(FilmEntity entity);

    @Mapping(target = "rating", source = "entity.rating.averageRating")
    @Mapping(target = "images", ignore = true)
    public abstract FullFilmInfo mapFullFilmInfo(FilmEntity entity);


    public abstract ImageFilmEntity createImageEntity(CreateImage image);

    @Mapping(target = "value", source = "entity", qualifiedByName = "getCommentValue")
    @Mapping(target = "previewCommentId", source = "entity.previewComment.id")
    protected abstract CommentInfo map(CommentEntity entity);

    @Named("getCommentValue")
    protected String getCommentValue(CommentEntity entity) {
        return Boolean.TRUE.equals(entity.getIsDeleted()) ? COMMENT_DELETED : entity.getValue();
    }

//    @Mapping(target = "isPrimary", source = "isPrimary", defaultValue = "false")
//    @Mapping(target = "image", source = "fileUri")
//    protected abstract Image map(ImageFilmEntity image);

    @Mapping(target = "image", source = "image")
    @Mapping(target = "id", source = "imageFilmEntity.id")
    @Mapping(target = "isPrimary", source = "imageFilmEntity.isPrimary")
    public abstract Image mapImage(String image, ImageFilmEntity imageFilmEntity);
}
