package com.fav.daengnyang.domain.target.entity;

import com.fav.daengnyang.domain.bankbook.entity.Bankbook;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Target {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long targetId;

    private String description;
    private int targetAmount;
    private String targetTitle;
    private int currentAmount;
    private boolean isDone;

    @ManyToOne(fetch = FetchType.LAZY) // Bankbook과의 다대일 관계
    @JoinColumn(name = "bankbook_id") // 외래 키: bankbook_id
    private Bankbook bankbook; // Target이 속한 Bankbook 객체

    @Builder
    public Target(Long targetId, String description, int targetAmount, String targetTitle, int currentAmount, boolean isDone, Bankbook bankbook) {
        this.targetId = targetId;
        this.description = description;
        this.targetAmount = targetAmount;
        this.targetTitle = targetTitle;
        this.currentAmount = currentAmount;
        this.isDone = isDone;
        this.bankbook = bankbook;
    }
}
