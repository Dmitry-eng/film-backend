package org.film.service;

import org.film.dto.request.CreateComment;

public interface CommentService {

    void addComment(CreateComment createComment);

    void removeComment(Long id);
}
