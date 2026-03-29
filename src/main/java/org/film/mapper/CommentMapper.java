package org.film.mapper;

import org.film.entity.AccountEntity;
import org.film.entity.CommentEntity;
import org.film.entity.FilmEntity;
import org.film.dto.request.CreateComment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @Mapping(source = "film", target = "film")
    @Mapping(source = "previewComment", target = "previewComment")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "createComment.value", target = "value")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "updateDatetime", ignore = true)
    public abstract CommentEntity addComment(CreateComment createComment, FilmEntity film, CommentEntity previewComment, AccountEntity author);

}
