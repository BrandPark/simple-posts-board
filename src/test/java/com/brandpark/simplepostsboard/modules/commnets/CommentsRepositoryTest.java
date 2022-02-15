package com.brandpark.simplepostsboard.modules.commnets;

import com.brandpark.simplepostsboard.*;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
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
}