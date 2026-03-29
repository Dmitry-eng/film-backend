package org.film.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.film.dto.request.account.AccountRegistration;
import org.film.dto.response.AccountInfo;
import org.film.entity.AccountEntity;
import org.film.entity.Role;
import org.film.exception.ServiceException;
import org.film.mapper.AccountMapper;
import org.film.repository.AccountRepository;
import org.film.repository.RoleRepository;
import org.film.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.film.exception.ServiceExceptionType.*;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Long USER_ROLE_ID = 2L;

    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final SecurityContextService securityContextService;
    private final RoleRepository roleRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void registrationAccount(AccountRegistration registration) {
        String password = passwordEncoder.encode(registration.getPassword());
        Role role = roleRepository.findById(USER_ROLE_ID)
                .orElseThrow(() -> new ServiceException(INTERNAL_SERVER_ERROR));
        AccountEntity accountEntity = accountMapper.map(registration, password, role);
        accountRepository.save(accountEntity);
    }

    @Override
    public void validateEmail(String email) {
        boolean exists = accountRepository.existsByEmail(email);
        if (exists) {
            throw new ServiceException(EMAIL_EXISTS);
        }
    }

    @Override
    @Transactional
    public AccountInfo getAccountInfo() {
        AccountEntity account = securityContextService.getAccount();
        return accountMapper.mapAccountInfo(account);
    }
}
