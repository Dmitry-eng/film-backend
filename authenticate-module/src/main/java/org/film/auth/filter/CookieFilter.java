package org.film.auth.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.film.auth.dto.JwtAuthentication;
import org.film.auth.tool.FilterUtil;
import org.film.auth.tool.JwtHelper;
import org.film.auth.util.Constant;
import org.film.db.entity.AccountEntity;
import org.film.db.repository.AccountRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

import static org.film.auth.util.Constant.SESSION_ID;

@Component
@RequiredArgsConstructor
public class CookieFilter extends GenericFilterBean {

    private final JwtHelper jwtHelper;
    private final AccountRepository accountRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = FilterUtil.getTokenFromCookie((HttpServletRequest) servletRequest);

        if (Objects.nonNull(token) && jwtHelper.validateAccessToken(token)) {
            Claims claims = jwtHelper.getAccessClaims(token);

            String sessionId = claims.get(SESSION_ID, String.class);
            String id = claims.getSubject();

            AccountEntity account = accountRepository.findById(Long.valueOf(id))
                    .orElseThrow(() -> new UsernameNotFoundException(Constant.ACCOUNT_NOT_FOUND));

            if (FilterUtil.validateSessionId(sessionId, account)) {
                JwtAuthentication jwtAuthentication = new JwtAuthentication(account);
                jwtAuthentication.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            }
        }
            filterChain.doFilter(servletRequest, servletResponse);

    }

}