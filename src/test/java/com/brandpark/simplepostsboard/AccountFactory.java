package com.brandpark.simplepostsboard;

import com.brandpark.simplepostsboard.modules.accounts.AccountRepository;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("test")
@Component
public class AccountFactory {
    @Autowired AccountRepository accountRepository;
    @Autowired PasswordEncoder passwordEncoder;

    public Accounts createAndPersistAccount(String nickname, String password) {
        String encodedPw = passwordEncoder.encode(password);
        return accountRepository.save(Accounts.createAccount(nickname, encodedPw));
    }

    public List<Accounts> createAndPersistAccountList(String nickname, String password, int size) {

        List<Accounts> ret = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ret.add(createAndPersistAccount(nickname + i, password));
        }

        return ret;
    }
}
