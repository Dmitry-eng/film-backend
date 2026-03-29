package org.film.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Jwt {

    private Token access;

    private Token refresh;

}