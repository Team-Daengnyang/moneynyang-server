package com.fav.daengnyang.domain.member.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.member.service.dto.request.AccountCreationHeaderRequest;
import com.fav.daengnyang.domain.member.service.dto.request.CreatedRequest;
import com.fav.daengnyang.domain.member.service.dto.request.MemberBankRequest;
import com.fav.daengnyang.domain.member.service.dto.response.AccountCreationResponse;
import com.fav.daengnyang.domain.member.service.dto.response.LoginResponse;
import com.fav.daengnyang.global.auth.dto.MemberAuthority;
import com.fav.daengnyang.global.auth.utils.JWTProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JWTProvider jwtProvider;

    @Value("${api.key}")
    private String apiKey;

    public LoginResponse createMember(CreatedRequest createdRequest) throws JsonProcessingException {
        // 1. 금융 API 연결
        MemberBankRequest memberBankRequest = createMemberBank(createdRequest);
        // 2. 예금 계좌 개설
        String depositAccount = createMemberAccount(memberBankRequest);
        // 3. DB에 회원 정보 저장
        Member member = save(createdRequest, depositAccount);
        // 4. accessToken 생성
        return createAccessToken(memberBankRequest, member);
    }

    // Member 저장
    private Member save(CreatedRequest createdRequest, String depositAccount){

        Member member = Member.createMember(createdRequest, depositAccount);
        return memberRepository.save(member);
    }


    private LoginResponse createAccessToken(MemberBankRequest memberBankRequest, Member member){
        String accessToken = jwtProvider.buildAccessToken(
                MemberAuthority.builder()
                        .memberId(member.getMemberId())
                        .userKey(memberBankRequest.getUserKey())
                        .build()
        );

        return LoginResponse.createLoginResponse(accessToken);
    }

    /*
        금융 API
    */
    // 회원가입 API
    private MemberBankRequest createMemberBank(CreatedRequest createdRequest) throws JsonProcessingException {

        // 1. body 객체 생성
        HashMap<String, String> body = new HashMap<>();
        body.put("apiKey", apiKey);
        body.put("userId", createdRequest.getEmail());

        // 2. HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 객체 생성
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 4. 외부 API 호출
        String url = "/member";

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            log.info("회원 가입 API 결과: " + response.getBody());
            return objectMapper.readValue(response.getBody(), MemberBankRequest.class);
    }

    // 계좌 생성 API
    private String createMemberAccount(MemberBankRequest memberBankResponse) throws JsonProcessingException {
        String accountTypeUniqueNo = "001-1-82fe5fa7eca441";

        log.info("계좌 생성 API 시작 " );
        // 1. body 객체 생성
        AccountCreationHeaderRequest header = AccountCreationHeaderRequest
                .createAccountCreationHeaderRequest(apiKey, memberBankResponse.getUserKey());

        HashMap<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountTypeUniqueNo", accountTypeUniqueNo);

        // 2. HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 4. 외부 API 호출
        String url = "/edu/demandDeposit/createDemandDepositAccount";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // 5. 외부 API 응답 처리
        log.info("계좌 생성 API 결과: " + response.getBody());
        return objectMapper.readValue(response.getBody(), AccountCreationResponse.class).getAccountNo();
    }
}
