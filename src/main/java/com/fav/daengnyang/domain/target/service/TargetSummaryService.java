package com.fav.daengnyang.domain.target.service;

import com.fav.daengnyang.domain.targetDetail.entity.BankbookDetail;
import com.fav.daengnyang.domain.target.entity.Target;
import com.fav.daengnyang.domain.target.repository.BankbookDetailRepository;
import com.fav.daengnyang.domain.target.repository.TargetRepository;
import com.fav.daengnyang.domain.target.service.dto.response.AchievedTargetInfoResponse;
import com.fav.daengnyang.domain.target.service.dto.response.TargetSummaryResponse;
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

    private final BankbookDetailRepository bankbookDetailRepository;
    private final TargetRepository targetRepository;

    // 전체 목표 요약 메소드
    public TargetSummaryResponse getTargetSummary(Long memberId) {
        // 유저의 모든 BankbookDetail 조회
        List<BankbookDetail> bankbookDetails = bankbookDetailRepository.findByMemberId(memberId);

        // 통계 값 계산
        int totalDeposits = bankbookDetails.size();
        int totalAmount = bankbookDetails.stream().mapToInt(BankbookDetail::getAmount).sum();

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
                        .completedDate(bankbookDetails.stream()
                                .filter(detail -> detail.getTarget().equals(target))
                                .map(BankbookDetail::getCreatedDate)
                                .max(LocalDate::compareTo) // 가장 최근의 날짜를 선택
                                .orElse(null))
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
