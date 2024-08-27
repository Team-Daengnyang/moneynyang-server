package com.fav.daengnyang.domain.target.service;

import com.fav.daengnyang.domain.account.entity.Account;
import com.fav.daengnyang.domain.account.repository.AccountRepository;
import com.fav.daengnyang.domain.target.entity.Target;
import com.fav.daengnyang.domain.target.repository.TargetRepository;
import com.fav.daengnyang.domain.target.service.dto.request.CreateTargetRequest;
import com.fav.daengnyang.domain.target.service.dto.response.TargetDetailResponse;
import com.fav.daengnyang.domain.target.service.dto.response.TargetResponse;
import com.fav.daengnyang.domain.targetDetail.entity.TargetDetail;
import com.fav.daengnyang.domain.targetDetail.repository.TargetDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TargetService {

    private final TargetRepository targetRepository;
    private final TargetDetailRepository targetDetailRepository;
    private final AccountRepository accountRepository;

    // 목표 생성 메소드
    @Transactional
    public Long createTarget(CreateTargetRequest request, Long memberId) {
        // memberId로 유저의 Account 조회
        Account account = accountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저에게 연동된 투자 계좌가 없습니다."));

        // Target 엔티티 생성
        Target target = Target.builder()
                .targetTitle(request.getTargetTitle())
                .description(request.getDescription())
                .targetAmount(request.getTargetAmount())
                .currentAmount(0)
                .isDone(false)
                .account(account)
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
                        .accountId(target.getAccount().getAccountId()) // Account ID 반환
                        .build())
                .toList();
    }


    public List<TargetDetailResponse> getTargetDetails(Long targetId) {
        // Target 조회
        Target target = targetRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 목표를 찾을 수 없습니다."));

        // 해당 Target에 대한 TargetDetail 리스트 조회
        List<TargetDetail> targetDetails = targetDetailRepository.findByTarget(target);

        // TargetDetail 리스트를 TargetDetailResponse로 변환하여 반환
        return targetDetails.stream()
                .map(detail -> TargetDetailResponse.builder()
                        .detailId(detail.getDetailId())
                        .amount(detail.getAmount())
                        .createdDate(detail.getCreatedDate())
                        .build())
                .collect(Collectors.toList());
    }
}