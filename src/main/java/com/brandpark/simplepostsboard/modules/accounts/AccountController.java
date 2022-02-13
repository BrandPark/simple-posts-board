package com.brandpark.simplepostsboard.modules.accounts;

import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.form.SignUpForm;
import lombok.RequiredArgsConstructor;
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
    private final AccountRepository accountRepository;

    @GetMapping("/sign-up")
    public String showSignupView(Model model) {

        model.addAttribute("signUpForm", new SignUpForm());

        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpForm form, Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("signUpForm", form);
            return "sign-up";
        }
        if (accountRepository.existsByNickname(form.getNickname())) {
            errors.rejectValue("nickname", "00000", "이미 존재하는 닉네임입니다.");
            return "sign-up";
        }

        SessionAccounts sessionAccounts = accountService.signUp(form);

        accountService.login(sessionAccounts);

        return "redirect:/";
    }
}
