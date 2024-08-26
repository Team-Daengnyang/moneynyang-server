package com.fav.daengnyang.domain.target.entity;

import com.fav.daengnyang.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "target")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Target {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "description")
    private String description;

    @Column(name = "target_amount")
    private Integer targetAmount;

    @Column(name = "target_title")
    private String targetTitle;

    @Column(name = "current_amount")
    private Integer currentAmount;

    @Column(name = "is_done")
    private Boolean isDone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder
    public Target(String description, Integer targetAmount, String targetTitle, Integer currentAmount, Boolean isDone, Account account) {
        this.description = description;
        this.targetAmount = targetAmount;
        this.targetTitle = targetTitle;
        this.currentAmount = currentAmount;
        this.isDone = isDone;
        this.account = account;
    }

    public Boolean isDone() {
        return isDone;
    }
}
