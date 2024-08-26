package com.fav.daengnyang.domain.target.repository;

import com.fav.daengnyang.domain.targetDetail.entity.BankbookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankbookDetailRepository extends JpaRepository<BankbookDetail, Long> {

    // 특정 memberId에 해당하는 모든 BankbookDetail 조회
    @Query("SELECT bd FROM BankbookDetail bd JOIN bd.target t JOIN t.bankbook b WHERE b.member.memberId = :memberId")
    List<BankbookDetail> findByMemberId(@Param("memberId") Long memberId);
}
