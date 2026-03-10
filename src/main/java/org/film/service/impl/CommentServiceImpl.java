package org.film.service.impl;

import lombok.RequiredArgsConstructor;
import org.film.dto.request.CreateComment;
import org.film.entity.AbstractEntity;
import org.film.entity.AccountEntity;
import org.film.entity.CommentEntity;
import org.film.entity.FilmEntity;
import org.film.execption.ServiceException;
import org.film.mapper.CommentMapper;
import org.film.repository.CommentRepository;
import org.film.repository.FilmRepository;
import org.film.service.CommentService;
import org.film.util.SecurityContextUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.film.execption.ServiceExceptionType.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final FilmRepository filmRepository;
    private final CommentMapper commentMapper;

    @Override
    public void addComment(CreateComment createComment) {
        CommentEntity previewComment = null;
        if (createComment.getPreviewCommentId() != null) {
            previewComment = commentRepository.findById(createComment.getPreviewCommentId())
                    .orElseThrow(() -> new ServiceException(COMMENT_NOT_FOUND));
        }

        FilmEntity film = filmRepository.findById(createComment.getFilmId())
                .filter(entity -> !Boolean.TRUE.equals(entity.getIsDeleted()))
                .orElseThrow(() -> new ServiceException(FILM_NOT_FOUND));

        if (previewComment != null) {
            Optional.of(previewComment)
                    .map(CommentEntity::getFilm)
                    .map(AbstractEntity::getId)
                    .filter(aLong -> film.getId().equals(aLong))
                    .orElseThrow(() -> new ServiceException(COMMENT_FILM_MISMATCH));
        }

        AccountEntity author = SecurityContextUtils.getAccount();

        CommentEntity comment = commentMapper.addComment(createComment, film, previewComment, author);

        commentRepository.save(comment);
    }

    @Override
    public void removeComment(Long id) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new ServiceException(COMMENT_NOT_FOUND));

        if (!SecurityContextUtils.isAdmin()) {
            AccountEntity accountEntity = SecurityContextUtils.getAccount();
            if (!accountEntity.getId().equals(comment.getAuthor().getId())) {
                throw new ServiceException(ACCESS_DENIED);
            }
        }

        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }

}
