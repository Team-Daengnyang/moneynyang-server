package com.fav.daengnyang.domain.memberDetail.repository;

import com.fav.daengnyang.domain.memberDetail.entity.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {

    @Query("SELECT md FROM MemberDetail md WHERE md.member.memberId = :memberId AND md.date = :date")
    Optional<MemberDetail> findByMemberIdAndDate(@Param("memberId") Long memberId, @Param("date") LocalDate date);
}
