package com.brandpark.simplepostsboard.modules.accounts;

import com.brandpark.simplepostsboard.modules.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Accounts extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accounts_id")
    private Long id;

    @Column(nullable = false)
    String nickname;

    @Column(nullable = false)
    String password;

    public static Accounts createAccount(String nickname, String password) {
        Accounts newAccounts = new Accounts();
        newAccounts.nickname = nickname;
        newAccounts.password = password;

        return newAccounts;
    }
}
