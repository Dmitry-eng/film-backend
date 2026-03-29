package org.film.service.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.film.entity.AccountEntity;
import org.film.entity.Role;
import org.film.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityContextService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountEntity getAccount() {
        return getAccountOptional()
                .orElse(null);
    }

    @Transactional
    public Boolean isAdmin() {
        return getAccountOptional()
                .map(AccountEntity::getRole)
                .map(Role::getName)
                .map("ADMIN"::equals)
                .orElse(false);
    }

    private Optional<AccountEntity> getAccountOptional() {
        return Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(object -> (Long) object)
                .flatMap(accountRepository::findById);
    }

}
