package com.fav.daengnyang.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fav.daengnyang.domain.member.service.dto.request.CreatedRequest;
import com.fav.daengnyang.domain.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "deposit_account")
    private String depositAccount;

    @Column(name = "created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @Column(name = "modified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modified;

    @Builder
    private Member(String email, String password, String depositAccount, String name, LocalDateTime created, LocalDateTime modified) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.depositAccount = depositAccount;
        this.created = created;
        this.modified = modified;
    }

    @PrePersist
    protected void onCreate() {
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modified = LocalDateTime.now();
    }

    public static Member createMember(CreatedRequest createdRequest, String depositAccount, String encodedPassword) {
        return Member.builder()
                .email(createdRequest.getEmail())
                .name(createdRequest.getName())
                .password(encodedPassword)
                .depositAccount(depositAccount)
                .build();
    }
}
