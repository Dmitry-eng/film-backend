package org.film.auth.tool;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.film.db.entity.AccountEntity;

import java.util.List;
import java.util.Optional;

import static org.film.auth.util.Constant.ACCESS_TOKEN;
import static org.film.auth.util.Constant.REFRESH_TOKEN;

@RequiredArgsConstructor
public class FilterUtil {


    public static boolean validateSessionId(String sessionId, AccountEntity account) {
        return sessionId.equals(account.getSessionId());
    }

    public static String getTokenFromCookie(HttpServletRequest request) {
        return Optional.of(request)
                .map(HttpServletRequest::getCookies)
                .map(List::of)
                .orElse(List.of())
                .stream()
                .filter(cookie -> ACCESS_TOKEN.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    public static String getRefreshTokenFromCookie(HttpServletRequest request) {
        return Optional.of(request)
                .map(HttpServletRequest::getCookies)
                .map(List::of)
                .orElse(List.of())
                .stream()
                .filter(cookie -> REFRESH_TOKEN.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}