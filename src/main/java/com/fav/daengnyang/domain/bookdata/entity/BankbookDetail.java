package com.fav.daengnyang.domain.bookdata.entity;

import com.fav.daengnyang.domain.bankbook.entity.Bankbook;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BankbookDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    private Integer amount;
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "bankbook_id")  // 외래 키 컬럼
    private Bankbook bankbook;  // Bankbook과의 관계 설정
}
