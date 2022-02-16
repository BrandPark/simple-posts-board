package com.brandpark.simplepostsboard.modules.blocks;

import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.LoginAccounts;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/accounts/{accountsId}/blocks")
@Controller
public class BlocksController {

    @GetMapping
    public String showBlockedListView(@LoginAccounts SessionAccounts loginAccounts, Model model) {

        model.addAttribute("loginAccounts", loginAccounts);

        return "list-blocked";
    }
}
