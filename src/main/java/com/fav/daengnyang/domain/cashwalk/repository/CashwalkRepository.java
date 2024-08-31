package com.fav.daengnyang.domain.cashwalk.repository;

import com.fav.daengnyang.domain.cashwalk.entity.Cashwalk;
import com.fav.daengnyang.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CashwalkRepository extends JpaRepository<Cashwalk, Long> {
    Optional<Cashwalk> findByMemberAndCreatedAt(Member member, LocalDate date);

    int countByMember(Member member);

    int countByMemberAndCreatedAtBetween(Member member, LocalDate oneMonthAgo, LocalDate today);
}
