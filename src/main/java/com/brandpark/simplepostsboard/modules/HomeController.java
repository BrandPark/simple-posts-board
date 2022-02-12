package com.brandpark.simplepostsboard.modules;

import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.LoginAccounts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {

    @GetMapping("/")
    public String showMainView(@LoginAccounts SessionAccounts account, Model model) {

        if (account != null) {
            model.addAttribute("account", account);
        }

        return "home";
    }

    @GetMapping("/login")
    public String showLoginView() {
        return "login";
    }
}
