package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.OrderBase;
import com.brandpark.simplepostsboard.modules.accounts.LoginAccounts;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showPostsListView(@LoginAccounts SessionAccounts loginAccounts, Model model) {

        model.addAttribute("orderBaseValues", OrderBase.values());
        return "list-posts";
    }

    @GetMapping("/{postsId}")
    public String showPostsDetailView() {
        return "detail-posts";
    }
}
