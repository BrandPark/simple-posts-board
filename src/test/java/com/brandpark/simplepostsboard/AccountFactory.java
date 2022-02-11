package com.brandpark.simplepostsboard;

import com.brandpark.simplepostsboard.api.Accounts;
import com.brandpark.simplepostsboard.modules.accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Component
public class AccountFactory {
    @Autowired AccountRepository accountRepository;

    public Accounts createAndPersistAccount(String nickname, String password) {
        return accountRepository.save(Accounts.createAccount(nickname, password));
    }
}
