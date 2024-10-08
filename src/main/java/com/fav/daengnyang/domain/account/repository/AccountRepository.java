package com.fav.daengnyang.domain.account.repository;

import com.fav.daengnyang.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    // memberId로 Account 조회
    @Query("SELECT b FROM Account b WHERE b.member.memberId = :memberId")
    Optional<Account> findByMemberId(@Param("memberId")Long memberId);
}
