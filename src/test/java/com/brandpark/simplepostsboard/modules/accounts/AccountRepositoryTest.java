package com.brandpark.simplepostsboard.modules.accounts;

import com.brandpark.simplepostsboard.MockMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@MockMvcTest
class AccountRepositoryTest {

    @Autowired AccountRepository accountRepository;

    @Test
    public void ExistsByNickname_True_When_Exists() throws Exception {

        // given
        String existsAccountNickname = "existsAccount";
        accountRepository.save(Accounts.createAccount(existsAccountNickname, "1q2w3e4r"));

        // when
        boolean actual = accountRepository.existsByNickname(existsAccountNickname);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    public void ExistsByNickname_False_When_NotExists() throws Exception {

        // given
        String existsAccountNickname = "NotExistsAccount";

        // when
        boolean actual = accountRepository.existsByNickname(existsAccountNickname);

        // then
        assertThat(actual).isFalse();
    }
}