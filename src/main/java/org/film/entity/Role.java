package org.film.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Table
@Entity
@Data
public class Role extends AbstractEntity {

    private String name;

}

