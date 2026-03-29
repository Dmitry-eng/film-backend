package org.film.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table
@Entity
public class CommentEntity extends AbstractEntity {

    @ManyToOne
    private FilmEntity film;

    @ManyToOne
    private CommentEntity previewComment;

    private String value;

    @ManyToOne
    private AccountEntity author;

}

