package com.brandpark.simplepostsboard.modules.accounts;

import com.brandpark.simplepostsboard.MockMvcTest;
import com.brandpark.simplepostsboard.api.Accounts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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