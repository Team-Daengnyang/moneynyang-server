package com.fav.daengnyang.domain.cashwalk.entity;

import com.fav.daengnyang.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cashwalk")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cashwalk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cashwalk_id")
    private Long cashwalkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "content")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "step")
    private Integer step;

    @Builder
    public Cashwalk(Member member, LocalDate createdAt, String content, String imageUrl, Integer step) {
        this.member = member;
        this.createdAt = createdAt;
        this.content = content;
        this.imageUrl = imageUrl;
        this.step = step;
    }
}
