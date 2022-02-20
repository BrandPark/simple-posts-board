package com.brandpark.simplepostsboard.api.posts;

import com.brandpark.simplepostsboard.modules.OrderBase;
import com.brandpark.simplepostsboard.api.posts.dto.PostsListResponse;
import com.brandpark.simplepostsboard.api.posts.dto.PostsResponse;
import com.brandpark.simplepostsboard.api.posts.dto.PostsSaveRequest;
import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.LoginAccounts;
import com.brandpark.simplepostsboard.modules.posts.Posts;
import com.brandpark.simplepostsboard.modules.posts.PostsRepository;
import com.brandpark.simplepostsboard.modules.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostsApiController {

    private final PostsService postsService;
    private final PostsRepository postsRepository;

    @PostMapping
    public Long registerPosts(@LoginAccounts SessionAccounts accounts, @RequestBody @Valid PostsSaveRequest req) {

        if (accounts == null) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        return postsService.registerPosts(req, accounts);
    }

    @GetMapping
    public PostsListResponse getAllPosts(@LoginAccounts SessionAccounts loginAccounts, @RequestParam(value = "orderBase") OrderBase orderBase) {

        List<Posts> allPosts = null;

        if (loginAccounts == null) {
            allPosts = postsRepository.findAllOrderedPostsWithAccounts(orderBase);
        } else {
            allPosts = postsRepository.findAllOrderedPostsWithAccountsExcludeBlockedPosts(loginAccounts.getId(), orderBase);
        }

        return new PostsListResponse(allPosts);
    }

    @GetMapping("/{postsId}")
    public PostsResponse getPosts(@PathVariable Long postsId) {

        Posts findPosts = postsService.viewPosts(postsId);

        return new PostsResponse(findPosts);
    }
}
