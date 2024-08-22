package com.fav.daengnyang.domain.target.service.dto;

import com.fav.daengnyang.domain.target.entity.Target;
import com.fav.daengnyang.domain.target.repository.TargetRepository;
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
