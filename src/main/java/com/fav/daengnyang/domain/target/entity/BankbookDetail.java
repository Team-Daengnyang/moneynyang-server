package com.fav.daengnyang.domain.target.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BankbookDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    private int amount;

    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Target target;

    @Builder
    public BankbookDetail(Long detailId, int amount, LocalDate createdDate, Target target) {
        this.detailId = detailId;
        this.amount = amount;
        this.createdDate = createdDate;
        this.target = target;
    }

}
