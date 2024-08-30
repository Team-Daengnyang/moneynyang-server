package com.fav.daengnyang.domain.memberDetail.service.dto;

import com.fav.daengnyang.domain.account.repository.AccountRepository;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.memberDetail.entity.MemberDetail;
import com.fav.daengnyang.domain.memberDetail.repository.MemberDetailRepository;
import com.fav.daengnyang.domain.memberDetail.service.dto.request.AnalyzeMonthlyRequest;
import com.fav.daengnyang.domain.memberDetail.service.dto.response.AnalyzeMonthlyResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.exception.CustomException;
import com.fav.daengnyang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberDetailService {

    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;

    public AnalyzeMonthlyResponse analyzeMonthly(MemberPrincipal memberPrincipal, AnalyzeMonthlyRequest analyzeMonthlyRequest) {
        // 0. 데이터 분석 년월
        YearMonth yearMonth = analyzeMonthlyRequest.getYearMonth();
        LocalDate startDate = yearMonth.atDay(1);

        // 1. 멤버 Id 찾기
        Long memberId = memberPrincipal.getMemberId();
        // 2. 멤버가 회원가입 한 날짜 이후인지 확인
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        YearMonth memberYearMonth = YearMonth.from(member.getCreated());
        if(yearMonth.isBefore(memberYearMonth)){
            // 회원가입 이전의 날짜를 조회한 경우
            return AnalyzeMonthlyResponse.createNoResponse();
        }

        // 3. 현재 월인지 확인
        YearMonth today = YearMonth.now();
        boolean isCurrentMonth = yearMonth.equals(today);

        // 4. 입력 날짜 데이터 불러오기
        MemberDetail memberDetail;
        if(isCurrentMonth) {
            // 현재 월일 경우 신한 DB 불러오기
            memberDetail = null;
        }
        else{
            // 현재 월이 아닐 경우
            memberDetail = memberDetailRepository
                   .findByMemberIdAndDate(memberId, startDate)
                   .orElseThrow(() -> new CustomException(ErrorCode.DATE_NOT_FOUND));
        }

        // 5. 입력 날짜 데이터 분석
        AnalyzeMonthlyResponse response = AnalyzeMonthlyResponse
                .createAnalyzeMonthlyResponse(memberDetail);

        // 6. 입력된 날짜 이전 날짜
        YearMonth previousYearMonth = yearMonth.minusMonths(1);
        LocalDate previousDate = previousYearMonth.atDay(1);
        if(previousYearMonth.isBefore(memberYearMonth)){
            // 입력된 날짜 이전 날짜의 데이터가 존재하지 않을 경우
            //response.updateAnlayze(-0.1);
        }
        else {
            // 입력된 날짜 이전 날짜의 데이터가 존재할 경우
            MemberDetail memberDetailBefore = memberDetailRepository
                    .findByMemberIdAndDate(memberId, previousDate)
                    .orElseThrow(() -> new CustomException(ErrorCode.DATE_NOT_FOUND));

            response.updateAnlayze(memberDetail.getTotal() - memberDetailBefore.getTotal());
        }
        return response;
    }
}

//    private AnalyzeMonthlyResponse analyzeCurrentMonth(Long memberId){
//
//        // 1. 계좌 번호 조회
//        Member member = memberRepository.findByMemberId(memberId)
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//        String account = member.getDepositAccount();
//
//        // 2. 금융 API 호출
//
//        return null;
//    }

