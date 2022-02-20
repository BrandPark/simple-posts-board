package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.*;
import com.brandpark.simplepostsboard.modules.OrderBase;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.blocks.BlockState;
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
    @Autowired BlocksFactory blocksFactory;
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
        List<Posts> result = postsRepository.findAllOrderedPostsWithAccounts(order);

        // then
        assertThat(result.size()).isEqualTo(allPostsCount);
        for (int i = 0; i < allPostsCount; i++) {
            Posts p = result.get(i);
            Accounts a = p.getAccounts();

            AssertUtil.assertObjectPropIsNotNull(p);
            AssertUtil.assertObjectPropIsNotNull(a);
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
        List<Posts> result = postsRepository.findAllOrderedPostsWithAccounts(order);

        // then
        assertThat(result.size()).isEqualTo(allPostsCount);
        for (int i = 0; i < allPostsCount; i++) {
            Posts p = result.get(i);
            Accounts a = p.getAccounts();

            AssertUtil.assertObjectPropIsNotNull(p);
            AssertUtil.assertObjectPropIsNotNull(a);
        }

        assertThat(result.get(0).getViewCount()).isGreaterThan(result.get(result.size() - 1).getViewCount());
    }

    @DisplayName("자신이 차단한 사람의 글을 제외하고 모든 게시물을 최신 순으로 가져오기")
    @Test
    public void FindAllPostsWithAccountOrderBy_CreatedDateDesc_Exclude_BlockedByMe() throws Exception {

        // given
        Accounts myAccounts = accountFactory.createAndPersistAccount("내 계정", "비밀번호");

        Accounts notBlockedAccounts = accountFactory.createAndPersistAccount("차단되지 않은 사람", "비밀번호");
        int notBlockedPostsCount = 10;
        postsFactory.createAndPersistPostsList("제목", "내용", notBlockedAccounts, notBlockedPostsCount);

        Accounts blockedAccounts = accountFactory.createAndPersistAccount("차단된 사람", "비밀번호");
        postsFactory.createAndPersistPosts("제목", "내용", blockedAccounts);

        OrderBase order = OrderBase.DATE_DESC;

        blocksFactory.createAndPersistRelation(myAccounts, blockedAccounts, BlockState.BLOCKED);

        em.flush();
        em.clear();

        // when
        List<Posts> result = postsRepository.findAllOrderedPostsWithAccountsExcludeBlockedPosts(myAccounts.getId(), order);

        // then
        assertThat(result.size()).isEqualTo(notBlockedPostsCount);

        for (int i = 0; i < notBlockedPostsCount; i++) {
            Posts p = result.get(i);
            Accounts a = p.getAccounts();

            AssertUtil.assertObjectPropIsNotNull(p);
            AssertUtil.assertObjectPropIsNotNull(a);
        }

        assertThat(result.get(0).getCreatedDate()).isAfterOrEqualTo(result.get(result.size() - 1).getCreatedDate());
    }

    @DisplayName("자신을 차단한 사람의 글을 제외하고 모든 게시물을 최신 순으로 가져오기")
    @Test
    public void FindAllPostsWithAccountOrderBy_CreatedDateDesc_Exclude_BlockedToMe() throws Exception {

        // given
        Accounts myAccounts = accountFactory.createAndPersistAccount("내 계정", "비밀번호");

        Accounts notBlockedAccounts = accountFactory.createAndPersistAccount("차단되지 않은 사람", "비밀번호");
        int notBlockedPostsCount = 10;
        postsFactory.createAndPersistPostsList("제목", "내용", notBlockedAccounts, notBlockedPostsCount);

        Accounts blockMeAccounts = accountFactory.createAndPersistAccount("나를 차단한 사람", "비밀번호");
        postsFactory.createAndPersistPosts("제목", "내용", blockMeAccounts);

        OrderBase order = OrderBase.DATE_DESC;

        blocksFactory.createAndPersistRelation(blockMeAccounts, myAccounts, BlockState.BLOCKED);

        em.flush();
        em.clear();

        // when
        List<Posts> result = postsRepository.findAllOrderedPostsWithAccountsExcludeBlockedPosts(myAccounts.getId(), order);

        // then
        assertThat(result.size()).isEqualTo(notBlockedPostsCount);

        for (int i = 0; i < notBlockedPostsCount; i++) {
            Posts p = result.get(i);
            Accounts a = p.getAccounts();

            AssertUtil.assertObjectPropIsNotNull(p);
            AssertUtil.assertObjectPropIsNotNull(a);
        }

        assertThat(result.get(0).getCreatedDate()).isAfterOrEqualTo(result.get(result.size() - 1).getCreatedDate());
    }

    @DisplayName("자신이 차단한 사람의 글을 제외하고 모든 글과 작성자들을 조회 순으로 가져오기")
    @Test
    public void FindAllPostsWithAccountOrderBy_ViewCountDesc_Exclude_BlockedByMe() throws Exception {

        // given
        Accounts myAccounts = accountFactory.createAndPersistAccount("내 계정", "비밀번호");

        Accounts notBlockedAccount = accountFactory.createAndPersistAccount("작성자", "비밀번호");
        int notBlockedPostsCount = 10;

        for (int i = 0; i < notBlockedPostsCount; i++) {
            int viewCount = notBlockedPostsCount - i;  // 최신 글일 수록 viewCount 가 낮게 설정.
            postsFactory.createAndPersistPosts("제목" + i, "내용" + i, notBlockedAccount, viewCount);
        }

        Accounts blockedByMe = accountFactory.createAndPersistAccount("내가 차단한 작성자", "비밀번호");
        postsFactory.createAndPersistPosts("차단한 사용자의 게시글", "내용", blockedByMe);

        blocksFactory.createAndPersistRelation(myAccounts, blockedByMe, BlockState.BLOCKED);

        OrderBase order = OrderBase.VIEW_DESC;

        em.flush();
        em.clear();

        // when
        List<Posts> result = postsRepository.findAllOrderedPostsWithAccountsExcludeBlockedPosts(myAccounts.getId(), order);

        // then
        assertThat(result.size()).isEqualTo(notBlockedPostsCount);
        for (int i = 0; i < notBlockedPostsCount; i++) {
            Posts p = result.get(i);
            Accounts a = p.getAccounts();

            AssertUtil.assertObjectPropIsNotNull(p);
            AssertUtil.assertObjectPropIsNotNull(a);
        }

        assertThat(result.get(0).getViewCount()).isGreaterThan(result.get(result.size() - 1).getViewCount());
    }

    @DisplayName("자신을 차단한 사람의 글을 제외하고 모든 글과 작성자들을 조회 순으로 가져오기")
    @Test
    public void FindAllPostsWithAccountOrderBy_ViewCountDesc_Exclude_BlockedMe() throws Exception {

        // given
        Accounts myAccounts = accountFactory.createAndPersistAccount("내 계정", "비밀번호");

        Accounts notBlockedAccount = accountFactory.createAndPersistAccount("작성자", "비밀번호");
        int notBlockedPostsCount = 10;

        for (int i = 0; i < notBlockedPostsCount; i++) {
            int viewCount = notBlockedPostsCount - i;  // 최신 글일 수록 viewCount 가 낮게 설정.
            postsFactory.createAndPersistPosts("제목" + i, "내용" + i, notBlockedAccount, viewCount);
        }

        Accounts blockedMe = accountFactory.createAndPersistAccount("내가 차단한 작성자", "비밀번호");
        postsFactory.createAndPersistPosts("차단한 사용자의 게시글", "내용", blockedMe);

        blocksFactory.createAndPersistRelation(blockedMe, myAccounts, BlockState.BLOCKED);

        OrderBase order = OrderBase.VIEW_DESC;

        em.flush();
        em.clear();

        // when
        List<Posts> result = postsRepository.findAllOrderedPostsWithAccountsExcludeBlockedPosts(myAccounts.getId(), order);

        // then
        assertThat(result.size()).isEqualTo(notBlockedPostsCount);
        for (int i = 0; i < notBlockedPostsCount; i++) {
            Posts p = result.get(i);
            Accounts a = p.getAccounts();

            AssertUtil.assertObjectPropIsNotNull(p);
            AssertUtil.assertObjectPropIsNotNull(a);
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