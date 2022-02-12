package com.brandpark.simplepostsboard;

import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Component
public class AccountFactory {
    @Autowired AccountRepository accountRepository;
    @Autowired PasswordEncoder passwordEncoder;

    public Accounts createAndPersistAccount(String nickname, String password) {
        String encodedPw = passwordEncoder.encode(password);
        return accountRepository.save(Accounts.createAccount(nickname, encodedPw));
    }
}
