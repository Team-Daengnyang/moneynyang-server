package com.fav.daengnyang.domain.chatbot.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chatbot")
@Getter
@Setter
@NoArgsConstructor
public class Chatbot {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "thread_id")
    private String threadId;

    @Builder
    public Chatbot(Long memberId, String threadId) {
        this.memberId = memberId;
        this.threadId = threadId;
    }
}
