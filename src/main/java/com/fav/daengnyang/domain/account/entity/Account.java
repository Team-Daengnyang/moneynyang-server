package com.fav.daengnyang.domain.account.entity;

import com.fav.daengnyang.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_title")
    private String accountTitle;

    @Column(name = "account_number")
    private String accountNumber;

    @Setter
    @Column(name = "account_color")
    private String accountColor;

    @Column(name = "account_image")
    private String accountImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Account(String accountTitle, String accountNumber, String accountColor, String accountImage, Member member) {
        this.accountTitle = accountTitle;
        this.accountNumber = accountNumber;
        this.accountColor = accountColor;
        this.accountImage = accountImage;
        this.member = member;
    }

    public void updateAccount(String accountColor, String accountImage) {

        this.accountColor = accountColor;
        this.accountImage =accountImage;
    }
}
