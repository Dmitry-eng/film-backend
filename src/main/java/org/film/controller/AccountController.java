package org.film.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.film.dto.request.account.AccountRegistration;
import org.film.dto.response.AccountInfo;
import org.film.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/registration")
    private void registrationAccount(@RequestBody @Valid AccountRegistration registration) {
        accountService.registrationAccount(registration);
    }

    @GetMapping("/validate/email/{email}")
    private void validateEmail(@PathVariable String email) {
        accountService.validateEmail(email);
    }

    @GetMapping("/info")
    private AccountInfo getAccountInfo() {
        return accountService.getAccountInfo();
    }

}
