package com.fav.daengnyang.domain.bankbook.repository;

import com.fav.daengnyang.domain.bankbook.entity.Bankbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BankbookRepository extends JpaRepository<Bankbook, Long> {
    Optional<Bankbook> findByBankbookNumber(String bankbookNumber);

    // memberId로 Bankbook 조회
    @Query("SELECT b FROM Bankbook b WHERE b.member.memberId = :memberId")
    Optional<Bankbook> findByMemberId(@Param("memberId")Long memberId);
}
