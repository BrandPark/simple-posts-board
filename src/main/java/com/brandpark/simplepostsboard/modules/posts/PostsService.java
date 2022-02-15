package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.api.posts.dto.PostsSaveRequest;
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
    public Long registerPosts(PostsSaveRequest req, SessionAccounts accounts) {

        Accounts writer = accountRepository.findById(accounts.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자의 계정이 올바르지 않습니다."));

        return postsRepository.save(req.toEntity(writer)).getId();
    }

    @Transactional
    public Posts viewPosts(Long postsId) {

        postsRepository.increasePostsViewCountById(postsId);

        Posts findPosts = postsRepository.findPostsWithAccountById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return findPosts;
    }
}
