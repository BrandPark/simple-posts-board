package com.brandpark.simplepostsboard.modules.accounts;

import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService implements UserDetailsService {

    private final PasswordEncoder pwEncoder;
    private final AccountRepository accountRepository;

    @Transactional
    public SessionAccounts signUp(SignUpForm form) {

        form.setPassword(pwEncoder.encode(form.getPassword()));

        Accounts newAccount = Accounts.createAccount(form.getNickname(), form.getPassword());

        accountRepository.save(newAccount);

        return convertToSessionAccounts(newAccount);
    }

    public void login(SessionAccounts sessionAccounts) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new CustomUser(sessionAccounts)
                , sessionAccounts.getPassword()
                , Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {

        Accounts account = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("nickname ??? '%s'??? ????????? ???????????? ????????????.", nickname)));

        SessionAccounts sessionAccounts = convertToSessionAccounts(account);

        return new CustomUser(sessionAccounts);
    }

    private SessionAccounts convertToSessionAccounts(Accounts account) {
        return SessionAccounts.builder()
                .id(account.getId())
                .nickname(account.getNickname())
                .password(account.getPassword())
                .build();
    }
}
