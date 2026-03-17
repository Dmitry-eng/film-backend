package org.film.auth.service;

import org.film.auth.dto.JwtRequest;

public interface AuthService {

    void login(JwtRequest authRequest);

    void refresh();

    void logout();
}
