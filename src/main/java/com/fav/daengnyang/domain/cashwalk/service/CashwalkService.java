package com.fav.daengnyang.domain.cashwalk.service;

import com.fav.daengnyang.domain.cashwalk.entity.Cashwalk;
import com.fav.daengnyang.domain.cashwalk.repository.CashwalkRepository;
import com.fav.daengnyang.domain.cashwalk.service.dto.request.CreateCashwalkRequest;
import com.fav.daengnyang.domain.cashwalk.service.dto.response.CashwalkResponse;
import com.fav.daengnyang.domain.cashwalk.service.dto.response.CashwalkStatsResponse;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.global.aws.service.AwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CashwalkService {

    private final MemberRepository memberRepository;
    private final CashwalkRepository cashwalkRepository;
    private final AwsService awsService;

    // 캐시워크 일지 등록하기
    @Transactional
    public Long createCashwalk(Long memberId, CreateCashwalkRequest createCashwalkRequest) {

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 유저가 없습니다."));

        // S3에 이미지 업로드
        // 이미지가 있을 경우에만 S3에 업로드하고 URL을 설정
        String image = null;
        if (createCashwalkRequest.getImage() != null && !createCashwalkRequest.getImage().isEmpty()) {
            image = awsService.uploadFile(createCashwalkRequest.getImage(), memberId);
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = awsService.getImageUrl(image);
        }

        // cashwalk 엔티티 생성
        Cashwalk cashwalk = Cashwalk.builder()
                .member(member)
                .createdAt(createCashwalkRequest.getCreatedAt())
                .content(createCashwalkRequest.getContent())
                .imageUrl(imageUrl)
                .step(0)
                .build();

        // 엔티티를 데이터베이스에 저장
        cashwalkRepository.save(cashwalk);

        // Cashwalk의 id만 반환
        return cashwalk.getCashwalkId();
    }

    // 이번 달의 모든 캐시워크 일지 조회
    public List<CashwalkResponse> getCashwalksForMonth(Long memberId, String date) {

        // 날짜 형변환
        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        YearMonth yearMonth = YearMonth.from(parsedDate);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<CashwalkResponse> responses = new ArrayList<>();

        for(LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            // 유저의 아이디로 유저 객체 조회
            Optional<Member> memberOpt = memberRepository.findByMemberId(memberId);
            Member member = memberOpt.get();

            // 유저가 해당 날짜에 한 캐시워크 일지 ID 조회
            Optional<Cashwalk> cashwalkOpt = cashwalkRepository.findByMemberAndCreatedAt(member, currentDate);

            if(cashwalkOpt.isPresent()) {
                Cashwalk cashwalk = cashwalkOpt.get();
                CashwalkResponse response = CashwalkResponse.builder()
                        .cashwalkId(cashwalk.getCashwalkId())
                        .date(cashwalk.getCreatedAt())
                        .diaryContent(cashwalk.getContent())
                        .imageUrl(cashwalk.getImageUrl())
                        .build();
                responses.add(response);
            }
        }

        // 해당 달에 작성한 일지가 없을 경우, null 값 리턴
        if (responses.isEmpty()) {
            return null;
        }

        return responses;
    }

    // 유저의 총 일지 작성 횟수, 일지 작성도 조회 메소드
    // 유저의 총 일지 작성 횟수, 달성도 반환 메소드
    public CashwalkStatsResponse getCashwalkStats(Long memberId) {
        // Member 객체 조회
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 유저가 없습니다."));

        // 총 일지 작성 횟수 계산
        int totalDiaries = cashwalkRepository.countByMember(member);

        // 현재 날짜
        LocalDate today = LocalDate.now();
        // 한 달 전 날짜
        LocalDate oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);

        // 회원가입일을 LocalDate로 변환
        LocalDate memberSince = member.getCreated().toLocalDate();

        // 실제 시작 날짜 결정 (한 달 전 날짜 또는 회원가입일 중 나중 날짜를 사용)
        LocalDate startDate = oneMonthAgo.isAfter(memberSince) ? oneMonthAgo : memberSince;

        // 지정된 기간 동안의 일지 작성 횟수 계산
        int periodDiaries = cashwalkRepository.countByMemberAndCreatedAtBetween(member, startDate, today);

        // 실제 기간의 일 수 계산
        long daysInPeriod = ChronoUnit.DAYS.between(startDate, today) + 1;

        // 달성률 계산 (정수로 반환)
        int achievementRate = (int) ((periodDiaries * 100) / daysInPeriod);

        // 결과 반환
        return CashwalkStatsResponse.builder()
                .totalDiaries(totalDiaries)
                .achievementRate(achievementRate)
                .build();
    }
}
