package com.fav.daengnyang.domain.account.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_code_id")
    private Long accountCodeId;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "account_name")
    private String accountName;

    @Builder
    public AccountCode(String accountCode, String accountName) {
        this.accountCode = accountCode;
        this.accountName = accountName;
    }
}
