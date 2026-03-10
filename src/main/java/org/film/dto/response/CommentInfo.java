package org.film.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentInfo {

    private Long id;

    private Long previewCommentId;

    private ShortAccountInfo author;

    private String value;

    private LocalDateTime createDateTime;

}
