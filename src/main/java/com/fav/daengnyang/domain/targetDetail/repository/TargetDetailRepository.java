package com.fav.daengnyang.domain.targetDetail.repository;

import com.fav.daengnyang.domain.target.entity.Target;
import com.fav.daengnyang.domain.targetDetail.entity.TargetDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetDetailRepository extends JpaRepository<TargetDetail, Long> {

    @Query("SELECT SUM(td.amount) FROM TargetDetail td WHERE td.target.account.accountId = :accountId")
    Integer findTotalAmountByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT COUNT(td) FROM TargetDetail td WHERE td.target.account.accountId = :accountId")
    Long countTransactionsByAccountId(@Param("accountId") Long accountId);

    // 특정 memberId에 해당하는 모든 TargetDetail 조회
    @Query("SELECT td FROM TargetDetail td JOIN td.target t JOIN t.account a WHERE a.member.memberId = :memberId")
    List<TargetDetail> findByMemberId(@Param("memberId") Long memberId);

    // 특정 Target에 대한 모든 TargetDetail을 조회
    List<TargetDetail> findByTarget(Target target);
}
