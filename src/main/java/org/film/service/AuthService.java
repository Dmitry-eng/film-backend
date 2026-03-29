package org.film.service;

import org.film.dto.JwtRequest;

public interface AuthService {

    void login(JwtRequest authRequest);

    void refresh();

    void logout();
}
