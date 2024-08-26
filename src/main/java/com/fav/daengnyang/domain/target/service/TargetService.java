package com.fav.daengnyang.domain.target.service;

import com.fav.daengnyang.domain.account.entity.Bankbook;
import com.fav.daengnyang.domain.account.repository.AccountRepository;
import com.fav.daengnyang.domain.target.entity.Target;
import com.fav.daengnyang.domain.target.repository.TargetRepository;
import com.fav.daengnyang.domain.target.service.dto.request.CreateTargetRequest;
import com.fav.daengnyang.domain.target.service.dto.response.TargetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TargetService {

    private final TargetRepository targetRepository;
    private final AccountRepository bankbookRepository;

    // 목표 생성 메소드
    @Transactional
    public Long createTarget(CreateTargetRequest request, Long memberId) {
        // memberId로 유저의 Bankbook 조회
        Bankbook bankbook = bankbookRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저에게 연동된 투자 계좌가 없습니다."));

        // Target 엔티티 생성
        Target target = Target.builder()
                .targetTitle(request.getTargetTitle())
                .description(request.getDescription())
                .targetAmount(request.getTargetAmount())
                .currentAmount(0)
                .isDone(false)
                .bankbook(bankbook) // 연관된 Bankbook 설정
                .build();

        // 엔티티를 데이터베이스에 저장
        targetRepository.save(target);

        // Target의 id만 반환
        return target.getTargetId();
    }

    // 유저의 모든 목표 조회 메소드
    public List<TargetResponse> findTargets(Long memberId) {

        // memberId로 Target 리스트 조회
        List<Target> targets = targetRepository.findAllByMemberId(memberId);

        // Target 리스트를 TargetResponse 리스트로 변환하여 반환
        return targets.stream()
                .map(target -> TargetResponse.builder()
                        .targetId(target.getTargetId())
                        .description(target.getDescription())
                        .targetAmount(target.getTargetAmount())
                        .targetTitle(target.getTargetTitle())
                        .currentAmount(target.getCurrentAmount())
                        .isDone(target.isDone())
                        .bankbookId(target.getBankbook().getBankbookId())
                        .build())
                .toList();
    }


}
