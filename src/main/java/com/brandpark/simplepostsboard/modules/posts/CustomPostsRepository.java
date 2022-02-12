package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.api.OrderBase;

import java.util.List;

public interface CustomPostsRepository {
    List<Posts> findAllPostsWithAccountsOrderBy(OrderBase orderBase);
}
