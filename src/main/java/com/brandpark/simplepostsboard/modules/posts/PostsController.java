package com.brandpark.simplepostsboard.modules.posts;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/posts")
@Controller
public class PostsController {

    @GetMapping("/create")
    public String showCreatePostsView() {
        return "create-posts";
    }

    @GetMapping("/list")
    public String showPostsListView() {
        return "list-posts";
    }
}
