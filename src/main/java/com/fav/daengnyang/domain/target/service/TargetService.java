package com.fav.daengnyang.domain.target.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.account.entity.Account;
import com.fav.daengnyang.domain.account.repository.AccountRepository;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TargetTransferService targetTransferService; // 계좌이체 금융 API를 사용하기 위한 TargetTransferService 주입

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
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
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
                        .startDate(target.getStartDate())
                        .endDate(target.getEndDate())
                        .accountId(target.getAccount().getAccountId()) // Account ID 반환
                        .build())
                .toList();
    }


    // 목표에 대한 세부정보(입금내역) 조회
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

    // 목표 삭제 메소드
    @Transactional
    public void deleteTarget(Long memberId, Long targetId, String userKey) throws JsonProcessingException {

        // Target 조회
        Target target = targetRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 목표를 찾을 수 없습니다."));

        // TargetDetail 리스트 조회 및 amount 합산
        List<TargetDetail> targetDetails = targetDetailRepository.findByTarget(target);
        int totalAmount = targetDetails.stream().mapToInt(TargetDetail::getAmount).sum();

        // TargetDetail 삭제
        targetDetailRepository.deleteAll(targetDetails);

        // Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));

        // 출금 계좌 (Target의 Bankbook)와 입금 계좌 (Member의 depositAccount) 정보 추출
        String withdrawalAccountNo = target.getAccount().getAccountNumber();
        String depositAccountNo = member.getDepositAccount();

        // 총 합계금액을 Member의 depositAccount로 이체
        if (totalAmount > 0) {
            targetTransferService.callTransferApi(depositAccountNo, withdrawalAccountNo, totalAmount, userKey); // 재사용
        }

        // Target 삭제
        targetRepository.delete(target);
    }
}