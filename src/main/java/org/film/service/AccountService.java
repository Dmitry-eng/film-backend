package org.film.service;

import org.film.dto.request.account.AccountRegistration;
import org.film.dto.response.AccountInfo;

public interface AccountService {

    void registrationAccount(AccountRegistration registration);

    void validateEmail(String email);

    AccountInfo getAccountInfo();
}
