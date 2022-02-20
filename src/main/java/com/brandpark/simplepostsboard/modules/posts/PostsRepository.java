package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.modules.OrderBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface PostsRepository extends JpaRepository<Posts, Long>, CustomPostsRepository {

    @Override
    List<Posts> findAllOrderedPostsWithAccounts(OrderBase orderBase);

    @Override
    List<Posts> findAllOrderedPostsWithAccountsExcludeBlockedPosts(Long loginAccountsId, OrderBase orderBase);

    @Query("SELECT p " +
            "FROM Posts p JOIN FETCH p.accounts WHERE p.id = :postsId")
    Optional<Posts> findPostsWithAccountById(@Param("postsId") Long postsId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Posts p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postsId")
    int increasePostsViewCountById(@Param("postsId") Long postsId);
}
