package org.film.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class AccountEntity extends AbstractEntity {

    private String name;

    private String email;

    @ManyToOne
    private Role role;


}
