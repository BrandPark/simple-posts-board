package com.brandpark.simplepostsboard.modules.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Accounts, Long> {

    boolean existsByNickname(String nickname);

    Optional<Accounts> findByNickname(String nickname);
}
