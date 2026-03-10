package org.film.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.film.dto.request.CreateComment;
import org.film.service.CommentService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Добавление комментария")
    @PostMapping
    public void addComment(@Valid @RequestBody(required = false) CreateComment createComment) {
        commentService.addComment(createComment);
    }

    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{id}")
    public void removeComment(@PathVariable Long id) {
        commentService.removeComment(id);
    }
}
