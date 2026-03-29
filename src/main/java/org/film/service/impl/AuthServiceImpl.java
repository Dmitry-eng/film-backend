package org.film.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.film.dto.Jwt;
import org.film.dto.JwtRequest;
import org.film.dto.Token;
import org.film.exception.SecurityException;
import org.film.service.AuthService;
import org.film.service.Cache;
import org.film.tool.JwtHelper;
import org.film.util.Constant;
import org.film.entity.AccountEntity;
import org.film.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.film.exception.type.SecurityExceptionType.*;
import static org.film.tool.FilterUtil.getRefreshTokenFromCookie;

@RequiredArgsConstructor
@Service
// TODO переписать решение
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final HttpServletResponse httpServletResponse;
    private final HttpServletRequest httpServletRequest;
    private final AccountRepository accountRepository;
    private final Cache<Set<String>, String> accountCacheToken;

    @Value("${domain}")
    private String domain;


    @Override
    public void login(JwtRequest authRequest) {
        AccountEntity account = accountRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new SecurityException(ACCOUNT_NOT_FOUND));
        if (passwordEncoder.matches(authRequest.getPassword(), account.getEncodePassword())) {

            Jwt jwtResponse = jwtHelper.generateToken(account);
            addedTokenInCookie(jwtResponse);

            accountCacheToken.add(account.getId().toString(), jwtResponse.getRefresh().getToken());
        } else {
            throw new SecurityException(INCORRECT_PASSWORD);
        }
    }

    @Override
    public void refresh() {
        String refreshToken = getRefreshTokenFromCookie(httpServletRequest);

        if (jwtHelper.validateRefreshToken(refreshToken)) {
            Claims claims = jwtHelper.getRefreshClaims(refreshToken);
            String id = claims.getSubject();
            Set<String> saveRefreshToken = accountCacheToken.get(id);
            if (saveRefreshToken.contains(refreshToken)) {
                AccountEntity account = accountRepository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new SecurityException(ACCOUNT_NOT_FOUND));

                Jwt jwtResponse = jwtHelper.generateToken(account);
                addedTokenInCookie(jwtResponse);

                accountCacheToken.remove(account.getId().toString(), refreshToken);
                accountCacheToken.add(account.getId().toString(), jwtResponse.getRefresh().getToken());
            }
        }
        throw new SecurityException(INVALID_JWT_TOKEN);
    }

    @Override
    public void logout() {
        Arrays.asList(Constant.ACCESS_TOKEN, Constant.REFRESH_TOKEN).forEach(name -> {
            Cookie cookie = new Cookie(name, null);
            cookie.setDomain(domain);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            httpServletResponse.addCookie(cookie);
        });
    }

    private void addedTokenInCookie(Jwt response) {
        Cookie accessToken = new Cookie(Constant.ACCESS_TOKEN, response.getAccess().getToken());
        Cookie refreshToken = new Cookie(Constant.REFRESH_TOKEN, response.getRefresh().getToken());

        accessToken.setDomain(domain);
        refreshToken.setDomain(domain);

        accessToken.setPath("/");
        refreshToken.setPath("/api/auth/refresh");

        long now = System.currentTimeMillis();

        int expiredAccess = Optional.of(response)
                .map(Jwt::getAccess)
                .map(Token::getExpired)
                .map(Date::getTime)
                .map(exp -> (int) ((exp - now) / 1000))
                .orElse(-1);

        int expiredRefresh = Optional.of(response)
                .map(Jwt::getRefresh)
                .map(Token::getExpired)
                .map(Date::getTime)
                .map(exp -> (int) ((exp - now) / 1000))
                .orElse(-1);


        accessToken.setMaxAge(expiredAccess);
        refreshToken.setMaxAge(expiredRefresh);

        httpServletResponse.addCookie(accessToken);
        httpServletResponse.addCookie(refreshToken);
    }
}
