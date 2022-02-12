package com.brandpark.simplepostsboard.modules.accounts;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.RepoTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepoTest
class AccountRepositoryTest {

    @Autowired AccountRepository accountRepository;
    @Autowired AccountFactory accountFactory;

    @DisplayName("닉네임이 존재하는지 확인 - True(있을 때)")
    @Test
    public void ExistsByNickname_True_When_Exists() throws Exception {

        // given
        String existsAccountNickname = "existsAccount";
        accountFactory.createAndPersistAccount(existsAccountNickname, "1q2w3e4r");

        // when
        boolean actual = accountRepository.existsByNickname(existsAccountNickname);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("닉네임이 존재하는지 확인 - False(없을 때)")
    @Test
    public void ExistsByNickname_False_When_NotExists() throws Exception {

        // given
        String existsAccountNickname = "NotExistsAccount";

        // when
        boolean actual = accountRepository.existsByNickname(existsAccountNickname);

        // then
        assertThat(actual).isFalse();
    }
    
    @DisplayName("닉네임으로 Accounts 가져오기 - 성공")
    @Test
    public void FindByNickname_Success_When_Exists() throws Exception {
    
        // given
        String existsAccountNickname = "existsAccount";
        accountFactory.createAndPersistAccount(existsAccountNickname, "1q2w3e4r");
                
        // when
        Optional<Accounts> byNickname = accountRepository.findByNickname(existsAccountNickname);

        // then
        assertThat(byNickname).isNotEmpty();
    }

    @DisplayName("닉네임으로 Accounts 가져오기 - 실패")
    @Test
    public void FindByNickname_Success_When_NotExists() throws Exception {

        // given
        String notExistsNickname = "notExistsNickname";

        // when
        Optional<Accounts> byNickname = accountRepository.findByNickname(notExistsNickname);

        // then
        assertThat(byNickname).isEmpty();
    }
}