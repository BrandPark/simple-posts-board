package com.brandpark.simplepostsboard.modules.accounts.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignUpForm {

    @NotBlank
    @Length(min=3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣A-Za-z0-9]{3,20}$", message = "문자와 숫자만 가능합니다.")
    private String nickname;

    @NotBlank
    @Length(min = 8, max = 30)
    private String password;
}
