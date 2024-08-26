package com.fav.daengnyang.domain.target.repository;

import com.fav.daengnyang.domain.target.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {

    // 특정 memberId를 가진 Bankbook의 모든 Target을 조회하는 JPQL 쿼리
    @Query("SELECT t FROM Target t JOIN t.account a WHERE a.member.memberId = :memberId")
    List<Target> findAllByMemberId(@Param("memberId") Long memberId);
}
