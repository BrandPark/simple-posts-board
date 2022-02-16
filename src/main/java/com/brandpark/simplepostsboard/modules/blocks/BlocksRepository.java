package com.brandpark.simplepostsboard.modules.blocks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface BlocksRepository extends JpaRepository<Blocks, Long> {

    Optional<Blocks> findByFromAccountsIdAndToAccountsId(Long fromAccountsId, Long toAccountsId);

    List<Blocks> findAllByFromAccountsIdAndBlockState(Long fromAccountsId, BlockState state);
}
