package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.modules.OrderBase;

import java.util.List;

public interface CustomPostsRepository {
    List<Posts> findAllOrderedPostsWithAccounts(OrderBase orderBase);
}
