package com.brandpark.simplepostsboard.api;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Accounts {

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
