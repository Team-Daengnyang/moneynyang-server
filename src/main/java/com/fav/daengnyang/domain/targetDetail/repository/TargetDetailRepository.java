package com.fav.daengnyang.domain.targetDetail.repository;

import com.fav.daengnyang.domain.targetDetail.entity.TargetDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetDetailRepository extends JpaRepository<TargetDetail, Long> {

    @Query("SELECT SUM(bd.amount) FROM TargetDetail bd WHERE bd.target.id = :bankbookId")
    Integer findTotalAmountByBankbookId(@Param("bankbookId") Long bankbookId);

    @Query("SELECT COUNT(bd) FROM TargetDetail bd WHERE bd.target.id = :bankbookId")
    Long countTransactionsByBankbookId(@Param("bankbookId") Long bankbookId);

    // 특정 memberId에 해당하는 모든 BankbookDetail 조회
    @Query("SELECT bd FROM TargetDetail bd JOIN bd.target t JOIN t.target b WHERE b.member.memberId = :memberId")
    List<TargetDetail> findByMemberId(@Param("memberId") Long memberId);
}
