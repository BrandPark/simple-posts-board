package com.brandpark.simplepostsboard.modules.commnets;

import com.brandpark.simplepostsboard.*;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.blocks.BlockState;
import com.brandpark.simplepostsboard.modules.posts.Posts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepoTest
class CommentsRepositoryTest {

    @Autowired CommentsRepository commentsRepository;
    @Autowired AccountFactory accountFactory;
    @Autowired PostsFactory postsFactory;
    @Autowired CommentsFactory commentsFactory;
    @Autowired BlocksFactory blocksFactory;
    @Autowired EntityManager entityManager;

    @DisplayName("PostsId로 Posts.Comments 모두 가져오기")
    @Test
    public void FindAllCommentsByPostsId() throws Exception {
    
        // given
        Accounts postsWriter = accountFactory.createAndPersistAccount("사용자", "1q2w3e4r");
        Posts posts = postsFactory.createAndPersistPosts("제목", "내용", postsWriter);

        Accounts commentsWriter = accountFactory.createAndPersistAccount("댓글 작성자", "1q2w3e4r");
        int allCommentsCount = 10;

        commentsFactory.createAndPersistCommentsList("댓글", commentsWriter, posts, allCommentsCount);

        entityManager.flush();
        entityManager.clear();

        // when
        List<Comments> result = commentsRepository.findAllByPostsId(posts.getId());

        // then
        assertThat(result.size()).isEqualTo(allCommentsCount);

        for (Comments c : result) {
            AssertUtil.assertObjectPropIsNotNull(c);
        }
    }

    @DisplayName("차단된 댓글은 제외하고 모든 댓글 조회")
    @Test
    public void FindAllComments_Exclude_BlockedComments() throws Exception {

        // given
        Accounts myAccounts = accountFactory.createAndPersistAccount("내 계정", "1q2w3e4r");
        Accounts blockedByMe = accountFactory.createAndPersistAccount("내가 차단한 계정", "1q2w3e4r");
        Accounts blockedMe = accountFactory.createAndPersistAccount("나를 차단한 계정", "1q2w3e4r");
        Accounts notBlock = accountFactory.createAndPersistAccount("차단하거나 차단되지 않은 계정", "1q2w3e4r");

        blocksFactory.createAndPersistRelation(myAccounts, blockedByMe, BlockState.BLOCKED);
        blocksFactory.createAndPersistRelation(blockedMe, myAccounts, BlockState.BLOCKED);

        Posts posts = postsFactory.createAndPersistPosts("게시글", "내용", myAccounts);

        int notBlockedCommentsCount = 10;
        commentsFactory.createAndPersistCommentsList("차단하거나 차단되지 않은 댓글", notBlock, posts, notBlockedCommentsCount);
        commentsFactory.createAndPersistComments("내가 차단한 사용자의 댓글", blockedByMe, posts);
        commentsFactory.createAndPersistComments("나를 차단한 사용자의 댓글", blockedMe, posts);

        // when
        List<Comments> result = commentsRepository.findAllNotBlockedCommentsByPostsId(myAccounts.getId(), posts.getId());

        // then
        assertThat(result.size()).isEqualTo(notBlockedCommentsCount);

        for (int i = 0; i < notBlockedCommentsCount; i++) {
            AssertUtil.assertObjectPropIsNotNull(result.get(i));
        }
    }
}