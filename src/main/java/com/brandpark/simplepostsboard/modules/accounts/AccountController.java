package com.brandpark.simplepostsboard.modules.accounts;

import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/accounts")
@Controller
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/sign-up")
    public String showSignupView(Model model) {

        model.addAttribute("signUpForm", new SignUpForm());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpForm form, Errors errors) {

        if (errors.hasErrors()) {
            return "sign-up";
        }

        SessionAccounts sessionAccounts = accountService.signUp(form);

        accountService.login(sessionAccounts);

        return "redirect:/";
    }
}
