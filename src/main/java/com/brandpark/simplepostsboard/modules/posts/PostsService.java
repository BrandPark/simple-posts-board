package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.api.posts.dto.SavePostsRequest;
import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.AccountRepository;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Long registerPosts(SavePostsRequest req, SessionAccounts accounts) {

        Accounts writer = accountRepository.findById(accounts.getId())
                .orElseThrow(() -> new IllegalStateException("없는 계정입니다."));

        return postsRepository.save(req.toEntity(writer)).getId();
    }
}