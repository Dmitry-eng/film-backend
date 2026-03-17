package org.film.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.film.auth.dto.JwtRequest;
import org.film.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public void login(@RequestBody @Valid JwtRequest authRequest) {
        authService.login(authRequest);
    }

    @PostMapping("/logout")
    public void logout() {
        authService.logout();
    }

    @PostMapping("/refresh")
    public void getNewTokenByRefresh() {
        authService.refresh();
    }

}
