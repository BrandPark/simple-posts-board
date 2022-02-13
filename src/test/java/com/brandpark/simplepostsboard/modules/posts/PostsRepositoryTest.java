package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.AssertUtil;
import com.brandpark.simplepostsboard.PostsFactory;
import com.brandpark.simplepostsboard.RepoTest;
import com.brandpark.simplepostsboard.api.OrderBase;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepoTest
class PostsRepositoryTest {

    @Autowired PostsRepository postsRepository;
    @Autowired PostsFactory postsFactory;
    @Autowired AccountFactory accountFactory;
    @Autowired EntityManager em;

    @DisplayName("모든 글과 작성자들을 최신 순으로 가져오기")
    @Test
    public void FindAllPostsWithAccountOrderBy_CreatedDateDesc() throws Exception {

        // given
        Accounts writer = accountFactory.createAndPersistAccount("작성자", "비밀번호");
        int allPostsCount = 10;
        postsFactory.createAndPersistPostsList("제목", "내용", writer, allPostsCount);

        OrderBase order = OrderBase.DATE_DESC;

        em.flush();
        em.clear();

        // when
        List<Posts> result = postsRepository.findAllPostsWithAccountsOrderBy(order);

        // then
        assertThat(result.size()).isEqualTo(allPostsCount);
        for (int i = 0; i < allPostsCount; i++) {
            Posts p = result.get(i);
            Accounts a = p.getAccounts();

            AssertUtil.assertObject(p);
            AssertUtil.assertObject(a);
        }

        assertThat(result.get(0).getCreatedDate()).isAfterOrEqualTo(result.get(result.size() - 1).getCreatedDate());
    }

    @DisplayName("모든 글과 작성자들을 조회 순으로 가져오기")
    @Test
    public void FindAllPostsWithAccountOrderBy_ViewCountDesc() throws Exception {

        // given
        Accounts writer = accountFactory.createAndPersistAccount("작성자", "비밀번호");
        int allPostsCount = 10;


        for (int i = 0; i < allPostsCount; i++) {
            int viewCount = allPostsCount - i;  // 최신 글일 수록 viewCount 가 낮게 설정.
            postsFactory.createAndPersistPosts("제목" + i, "내용" + i, writer, viewCount);
        }

        OrderBase order = OrderBase.VIEW_DESC;

        em.flush();
        em.clear();

        // when
        List<Posts> result = postsRepository.findAllPostsWithAccountsOrderBy(order);

        // then
        assertThat(result.size()).isEqualTo(allPostsCount);
        for (int i = 0; i < allPostsCount; i++) {
            Posts p = result.get(i);
            Accounts a = p.getAccounts();

            AssertUtil.assertObject(p);
            AssertUtil.assertObject(a);
        }

        assertThat(result.get(0).getViewCount()).isGreaterThan(result.get(result.size() - 1).getViewCount());
    }

    @DisplayName("PostsId로 Posts와 Accounts 같이 조회하기")
    @Test
    public void FindPostsWithAccountsById() throws Exception {

        // given
        Accounts writer = accountFactory.createAndPersistAccount("작성자", "1q2w3e4r");
        Long postsId = postsFactory.createAndPersistPosts("제목", "내용", writer).getId();

        // when
        Optional<Posts> result = postsRepository.findPostsWithAccountById(postsId);

        // then
        assertThat(result).isNotEmpty();
    }

    @DisplayName("PostsId로 Posts의 viewCount 증가시키기")
    @Test
    public void IncreasePostsViewCountById() throws Exception {

        // given
        Accounts writer = accountFactory.createAndPersistAccount("작성자", "1q2w3e4r");
        Posts savedPosts = postsFactory.createAndPersistPosts("제목", "내용", writer);
        Long postsId = savedPosts.getId();

        assertThat(savedPosts.getViewCount()).isEqualTo(0);

        // when
        int affectedCount = postsRepository.increasePostsViewCountById(postsId);

        // then
        assertThat(affectedCount).isEqualTo(1);

        Posts updatedPosts = postsRepository.findById(postsId).get();
        assertThat(updatedPosts.getViewCount()).isEqualTo(1);
    }
}