package com.fav.daengnyang.domain.cashwalk.service.dto;

import com.fav.daengnyang.domain.cashwalk.entity.Cashwalk;
import com.fav.daengnyang.domain.cashwalk.repository.CashwalkRepository;
import com.fav.daengnyang.domain.cashwalk.service.dto.request.CashwalkRequest;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CashwalkService {

    private final MemberRepository memberRepository;
    private final CashwalkRepository cashwalkRepository;

    // 캐시워크 일지 등록하기
    public Long createCashwalk(Long memberId, CashwalkRequest cashwalkRequest) {

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 유저가 없습니다."));

        // cashwalk 엔티티 생성
        Cashwalk cashwalk = Cashwalk.builder()
                .member(member)
                .createdAt(cashwalkRequest.getCreatedAt())
                .content(cashwalkRequest.getContent())
                .step(0)
                .build();

        // 엔티티를 데이터베이스에 저장
        cashwalkRepository.save(cashwalk);

        // Cashwalk의 id만 반환
        return cashwalk.getCashwalkId();
    }
}
