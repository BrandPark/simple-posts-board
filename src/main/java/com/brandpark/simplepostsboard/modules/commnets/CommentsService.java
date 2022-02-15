package com.brandpark.simplepostsboard.modules.commnets;

import com.brandpark.simplepostsboard.api.comments.dto.CommentsSaveRequest;
import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.AccountRepository;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.posts.Posts;
import com.brandpark.simplepostsboard.modules.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentsService {

    private final AccountRepository accountRepository;
    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;

    @Transactional
    public Long registerComments(SessionAccounts accounts, Long postsId, CommentsSaveRequest reqData) {
        Accounts writer = accountRepository.findById(accounts.getId())
                .orElseThrow(() -> new IllegalStateException("사용자의 계정이 올바르지 않습니다."));

        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return commentsRepository.save(Comments.createComments(reqData.getContent(), writer, posts)).getId();
    }
}
