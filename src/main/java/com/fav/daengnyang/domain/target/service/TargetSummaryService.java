package com.fav.daengnyang.domain.target.service;

import com.fav.daengnyang.domain.targetDetail.entity.TargetDetail;
import com.fav.daengnyang.domain.target.entity.Target;
import com.fav.daengnyang.domain.target.repository.TargetRepository;
import com.fav.daengnyang.domain.target.service.dto.response.AchievedTargetInfoResponse;
import com.fav.daengnyang.domain.target.service.dto.response.TargetSummaryResponse;
import com.fav.daengnyang.domain.targetDetail.entity.TargetDetail;
import com.fav.daengnyang.domain.targetDetail.repository.TargetDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TargetSummaryService {

    private final TargetDetailRepository targetDetailRepository;
    private final TargetRepository targetRepository;

    // 전체 목표 요약 메소드
    public TargetSummaryResponse getTargetSummary(Long memberId) {
        // 유저의 모든 BankbookDetail 조회
        List<TargetDetail> targetDetails = targetDetailRepository.findByMemberId(memberId);

        // 통계 값 계산
        int totalDeposits = targetDetails.size();
        int totalAmount = targetDetails.stream().mapToInt(TargetDetail::getAmount).sum();

        // 달성한 목표 정보 계산
        List<Target> targets = targetRepository.findAllByMemberId(memberId);
        int achievedTargetsCount = 0;
        List<AchievedTargetInfoResponse> achievedTargets = new ArrayList<>();

        for (Target target : targets) {
            if(target.isDone()){
                achievedTargetsCount++;
                AchievedTargetInfoResponse achievedTargetInfoResponse = AchievedTargetInfoResponse.builder()
                        .targetTitle(target.getTargetTitle())
                        .targetAmount(target.getTargetAmount())
                        .completedDate(target.getEndDate())
                        .build();
                achievedTargets.add(achievedTargetInfoResponse);
            }
        }

        // 평균 계산
        double averageDepositsPerTargets = targets.isEmpty() ? 0 : (double) totalDeposits / targets.size();
        double averageAmountPerDeposit = totalDeposits == 0 ? 0 : (double) totalAmount / totalDeposits;

        // 결과 반환
        return TargetSummaryResponse.builder()
                .averageDepositsPerTarget(averageDepositsPerTargets)
                .averageAmountPerDeposit(averageAmountPerDeposit)
                .achievedTargetsCount(achievedTargetsCount)
                .achievedTargets(achievedTargets)
                .build();
    }
}