package com.fav.daengnyang.domain.chatbot.repository;

import com.fav.daengnyang.domain.chatbot.entity.Chatbot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotRepository extends JpaRepository<Chatbot, Long> {
}
