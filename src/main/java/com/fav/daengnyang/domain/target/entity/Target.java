package com.fav.daengnyang.domain.target.entity;

import com.fav.daengnyang.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "target")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
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

    @Column(name = "is_withdrawed")
    private Boolean isWithdrawed;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder
    public Target(String description, Integer targetAmount, String targetTitle, Integer currentAmount, Boolean isDone, Boolean isWithdrawed, LocalDate startDate, LocalDate endDate, Account account) {
        this.description = description;
        this.targetAmount = targetAmount;
        this.targetTitle = targetTitle;
        this.currentAmount = currentAmount;
        this.isDone = isDone;
        this.isWithdrawed = isWithdrawed;
        this.startDate = startDate;
        this.endDate = endDate;
        this.account = account;
    }

    public Boolean isDone() {
        return isDone;
    }

    public Boolean isWithdrawed() {
        return isWithdrawed;
    }

    public static Target createTargetTitle(String targetTitle) {
        return Target.builder()
                .targetTitle(targetTitle)
                .build();
    }
}
