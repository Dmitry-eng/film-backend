package org.film.auth.dto;

import lombok.Data;
import org.film.db.entity.AccountEntity;
import org.film.db.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class JwtAuthentication implements Authentication {

    private Boolean authenticated;

    private String email;

    private String role;

    private Boolean isBlocked;

    private Boolean isDeleted;

    private Long id;

    public JwtAuthentication(AccountEntity account) {
        this.email = account.getEmail();

        this.id = account.getId();

        this.role = Optional.of(account)
                .map(AccountEntity::getRole)
                .map(Role::getName)
                .orElse(null);

        this.isBlocked = Optional.of(account)
                .map(AccountEntity::getIsBlocked)
                .orElse(false);

        this.isDeleted = Optional.of(account)
                .map(AccountEntity::getIsDeleted)
                .orElse(false);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.of(role)
                .map(role ->  "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .map(simpleGrantedAuthority -> Set.of(simpleGrantedAuthority))
                .orElseThrow();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return id;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated && !isDeleted && !isBlocked;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return email;
    }
}