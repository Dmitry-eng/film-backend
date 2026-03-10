package org.film.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table
@Entity
public class Role extends AbstractEntity {

    private String name;

}

