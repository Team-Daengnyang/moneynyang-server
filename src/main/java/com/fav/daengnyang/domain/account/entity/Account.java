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

    @Column(name = "account_image")
    private String accountImage;

    @Setter
    @Column(name = "account_color")
    private String accountColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Account(String accountTitle, String accountNumber, String accountImage, String accountColor, Member member) {
        this.accountTitle = accountTitle;
        this.accountNumber = accountNumber;
        this.accountImage = accountImage;
        this.accountColor = accountColor;
        this.member = member;
    }

}