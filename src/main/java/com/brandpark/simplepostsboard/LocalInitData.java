package com.brandpark.simplepostsboard;

import com.brandpark.simplepostsboard.api.Accounts;
import com.brandpark.simplepostsboard.modules.accounts.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class LocalInitData {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        String nickname = "admin";
        String password = "1q2w3e4r";

        accountRepository.save(Accounts.createAccount(nickname, passwordEncoder.encode(password)));
    }
}
