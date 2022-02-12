package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.api.OrderBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostsRepository extends JpaRepository<Posts, Long>, CustomPostsRepository {

    @Override
    List<Posts> findAllPostsWithAccountsOrderBy(OrderBase orderBase);
}
