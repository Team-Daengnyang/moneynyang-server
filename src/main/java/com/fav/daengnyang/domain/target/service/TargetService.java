package com.fav.daengnyang.domain.target.service;

import com.fav.daengnyang.domain.account.entity.Account;
import com.fav.daengnyang.domain.account.repository.AccountRepository;
import com.fav.daengnyang.domain.pet.entity.Pet;
import com.fav.daengnyang.domain.pet.repository.PetRepository;
import com.fav.daengnyang.domain.target.entity.Target;
import com.fav.daengnyang.domain.target.repository.TargetRepository;
import com.fav.daengnyang.domain.target.service.dto.request.CreateTargetRequest;
import com.fav.daengnyang.domain.target.service.dto.response.TargetDetailResponse;
import com.fav.daengnyang.domain.target.service.dto.response.TargetResponse;
import com.fav.daengnyang.domain.targetDetail.entity.TargetDetail;
import com.fav.daengnyang.domain.targetDetail.repository.TargetDetailRepository;
import com.fav.daengnyang.global.exception.CustomException;
import com.fav.daengnyang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TargetService {

    private final TargetRepository targetRepository;
    private final TargetDetailRepository targetDetailRepository;
    private final AccountRepository accountRepository;
    private final PetRepository petRepository;
    private final Random random = new Random();

    // 타겟 추천 메소드
    public HashMap<String, String> recommendTarget(Long memberId){
        // 1. 반려동물 정보 찾기
        Pet pet = petRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PET_NOT_FOUND));

        // 2. 다른 목표 반려동물 찾기
        List<Target> targetList = targetRepository.findAllTargetsExceptMine(memberId, pet.getPetType());

        // 3. 생일 관련
        if(pet.getPetBirth() != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            LocalDate today = LocalDate.now();
            LocalDate birthday = LocalDate.parse(pet.getPetBirth(), formatter).withYear(today.getYear());

            LocalDate oneWeekAgo = today.minusWeeks(1);
            if(today.isBefore(birthday) && today.isAfter(oneWeekAgo)){
                Period period = Period.between(today, birthday);
                long daysBetween = period.getDays();
                for(int i = 0; i < targetList.size() / 5; i++){
                    Target t = Target.createTargetTitle("생일 " + daysBetween + "일");
                    targetList.add(t);
                }
            }
        }

        // 4. 목표 추천
        int index = random.nextInt(targetList.size());
        Target target = targetList.get(index);

        // 5. response
        HashMap<String, String> response = new HashMap<>();
        response.put("recommend", target.getTargetTitle());
        return response;
    }


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
                .isWithdrawed(false)
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

        // Target 리스트들 중에서 isWithdrawed가 false인 목표들만 필터링
        List<Target> activeTargets = targets.stream()
                .filter(target -> !target.isWithdrawed())
                .collect(Collectors.toList());

        // Target 리스트를 TargetResponse 리스트로 변환하여 반환
        return activeTargets.stream()
                .map(target -> TargetResponse.builder()
                        .targetId(target.getTargetId())
                        .description(target.getDescription())
                        .targetAmount(target.getTargetAmount())
                        .targetTitle(target.getTargetTitle())
                        .currentAmount(target.getCurrentAmount())
                        .isDone(target.isDone())
                        .isWithdraw(target.getIsWithdrawed())
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
}