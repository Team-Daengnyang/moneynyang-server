package com.fav.daengnyang.domain.bankbook.entity;

import com.fav.daengnyang.domain.bookdata.entity.BookdataDetail;
import com.fav.daengnyang.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Bankbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankbookId;

    private String bankbookTitle;
    private String bankbookNumber;
    private String bankbookImage;
    private String bankbookColor;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "bankbook", cascade = CascadeType.ALL)
    private List<BookdataDetail> bookdataDetails;  // 'mappedBy' 속성을 'bankbook'으로 설정
}
