package com.brandpark.simplepostsboard.modules.accounts;

import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class CustomUser extends User {

    private SessionAccounts account;

    public CustomUser(SessionAccounts account) {
        super(account.getNickname()
                , account.getPassword()
                , Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        this.account = account;
    }
}
