package com.fav.daengnyang.domain.targetDetail.entity;

import com.fav.daengnyang.domain.target.entity.Target;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "target_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Target target;

    @Builder
    public TargetDetail(Integer amount, LocalDate createdDate, Target target) {
        this.amount = amount;
        this.createdDate = createdDate;
        this.target = target;
    }
}
