package com.fav.daengnyang.domain.cashwalk.service;

import com.fav.daengnyang.domain.cashwalk.entity.Cashwalk;
import com.fav.daengnyang.domain.cashwalk.repository.CashwalkRepository;
import com.fav.daengnyang.domain.cashwalk.service.dto.request.CashwalkRequest;
import com.fav.daengnyang.domain.cashwalk.service.dto.response.CashwalkResponse;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CashwalkService {

    private final MemberRepository memberRepository;
    private final CashwalkRepository cashwalkRepository;

    // 캐시워크 일지 등록하기
    @Transactional
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
                CashwalkResponse response = CashwalkResponse.builder()
                        .cashwalkId(cashwalkOpt.map(Cashwalk::getCashwalkId).orElse(null))
                        .date(cashwalkOpt.map(Cashwalk::getCreatedAt).orElse(null))
                        .diaryContent(cashwalkOpt.map(Cashwalk::getContent).orElse(null))
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
}
