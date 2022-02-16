package com.brandpark.simplepostsboard;

import com.brandpark.simplepostsboard.modules.accounts.AccountRepository;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("local")
@RequiredArgsConstructor
@Component
public class LocalInitData {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        accountRepository.save(Accounts.createAccount("admin", passwordEncoder.encode("1q2w3e4r")));
        accountRepository.save(Accounts.createAccount("user1", passwordEncoder.encode("1q2w3e4r")));
        accountRepository.save(Accounts.createAccount("user2", passwordEncoder.encode("1q2w3e4r")));
    }
}
