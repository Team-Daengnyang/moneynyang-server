package com.fav.daengnyang.domain.memberDetail.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.memberDetail.entity.MemberDetail;
import com.fav.daengnyang.domain.memberDetail.repository.MemberDetailRepository;
import com.fav.daengnyang.domain.memberDetail.service.dto.request.AnalyzeMonthlyRequest;
import com.fav.daengnyang.domain.memberDetail.service.dto.request.InquireTransactionHeader;
import com.fav.daengnyang.domain.memberDetail.service.dto.response.AnalyzeMonthlyResponse;
import com.fav.daengnyang.domain.memberDetail.service.dto.response.InquireTransactionResponse;
import com.fav.daengnyang.domain.memberDetail.service.dto.response.TransResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.exception.CustomException;
import com.fav.daengnyang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberDetailService {

    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.key}")
    private String apiKey;

    public AnalyzeMonthlyResponse analyzeMonthly(MemberPrincipal memberPrincipal, AnalyzeMonthlyRequest analyzeMonthlyRequest) throws JsonProcessingException {
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
            return AnalyzeMonthlyResponse.createNoResponse(false);
        }

        // 3. 현재 월인지 확인
        YearMonth today = YearMonth.now();
        boolean isCurrentMonth = yearMonth.equals(today);

        // 4. 입력 날짜 데이터 불러오기
        MemberDetail memberDetail;
        if(isCurrentMonth) {
            // 현재 월일 경우 신한 DB 불러오기
            InquireTransactionResponse inquireTransactionResponse = getCurrentDataList(member.getDepositAccount(), memberPrincipal.getUserKey(), yearMonth);
            memberDetail = getCurrentData(inquireTransactionResponse);
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
            response.updateAnlayze(0, false);
        }
        else {
            // 입력된 날짜 이전 날짜의 데이터가 존재할 경우
            MemberDetail memberDetailBefore = memberDetailRepository
                    .findByMemberIdAndDate(memberId, previousDate)
                    .orElseThrow(() -> new CustomException(ErrorCode.DATE_NOT_FOUND));

            response.updateAnlayze(memberDetail.getTotal() - memberDetailBefore.getTotal(), true);
        }
        return response;
    }

    private MemberDetail getCurrentData(InquireTransactionResponse inquireTransactionResponse){

        // 0. 세팅 데이터
        int pet = 0;
        int shopping = 0;
        int transportation = 0;
        int food = 0;
        int others = 0;
        int total = 0;
        int petPayCount = 0;

        List<TransResponse> list = inquireTransactionResponse.getTransactionList();

        for(TransResponse transactionResponse : list){
            String memo = transactionResponse.getTransactionMemo();
            Integer price = Integer.valueOf(transactionResponse.getTransactionBalance());
            if(memo.contains("식비")){
                food += price;
            }
            else if(memo.contains("쇼핑")){
                shopping += price;
            }
            else if(memo.contains("반려")){
                pet += price;
                petPayCount ++;
            }
            else if(memo.contains("교통")){
                transportation += price;
            }
            else{
                others += price;
            }
        }

        total = food + shopping + transportation + pet + others;
        return MemberDetail.createMemberDetail(pet, food, shopping, transportation, others, total, petPayCount, null, null);
    }

    private InquireTransactionResponse getCurrentDataList(String account, String userKey, YearMonth yearMonth) throws JsonProcessingException {
        // 0. 조회 시작, 끝 날짜
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 1. body 객체 생성
        HashMap<String, Object> body = new HashMap<>();

        InquireTransactionHeader header = InquireTransactionHeader.createInquireTransactionHeader(apiKey, userKey);
        body.put("Header", header);
        body.put("accountNo", account);
        body.put("startDate", startDate.format(formatter)); // 금액
        body.put("endDate", endDate.format(formatter));
        body.put("transactionType", "D");
        body.put("orderByType", "ASC");

        // 2. HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 4. 외부 API 호출
        String url = "/edu/demandDeposit/inquireTransactionHistoryList";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        log.info("계좌 거래 내역 조회 API 결과: " + response.getBody());
        return objectMapper.readValue(response.getBody(), InquireTransactionResponse.class);
    }

}
