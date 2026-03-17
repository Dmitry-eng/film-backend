package org.film.auth.dto;

import lombok.Data;

@Data
public class ExpirationTime {

    private Integer day = 0;

    private Integer hour = 0;

    private Integer minute = 15;

    private Integer second = 0;

}