package com.fav.daengnyang.domain.member.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.member.service.dto.request.AccountCreationHeaderRequest;
import com.fav.daengnyang.domain.member.service.dto.request.CreatedRequest;
import com.fav.daengnyang.domain.member.service.dto.request.LoginRequest;
import com.fav.daengnyang.domain.member.service.dto.response.AccountCreationResponse;
import com.fav.daengnyang.domain.member.service.dto.response.LoginResponse;
import com.fav.daengnyang.domain.member.service.dto.response.MemberBankResponse;
import com.fav.daengnyang.global.auth.dto.MemberAuthority;
import com.fav.daengnyang.global.auth.utils.JWTProvider;
import com.fav.daengnyang.global.exception.CustomException;
import com.fav.daengnyang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        MemberBankResponse memberBankResponse = createMemberBank(createdRequest);
        // 2. 예금 계좌 개설
        String depositAccount = createMemberAccount(memberBankResponse);
        // 3. DB에 회원 정보 저장
        Member member = save(createdRequest, depositAccount);
        // 4. accessToken 생성
        return createAccessToken(memberBankResponse, member);
    }

    public LoginResponse login(LoginRequest loginRequest) throws JsonProcessingException {
        // 1. 아이디 존재 확인
        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USERNOTFOUND));

        // 2. 비밀번호 일치 확인
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALIDPASSWORD);
        }

        // 3. 신한 API에서 userKey 가져오기
        MemberBankResponse memberBankResponse = loginMemberBank(member);

        // 4. accessToken 생성
        return createAccessToken(memberBankResponse, member);
    }

    // Member 저장
    private Member save(CreatedRequest createdRequest, String depositAccount){

        Member member = Member.createMember(createdRequest, depositAccount);
        return memberRepository.save(member);
    }

    private LoginResponse createAccessToken(MemberBankResponse memberBankResponse, Member member){
        String accessToken = jwtProvider.buildAccessToken(
                MemberAuthority.builder()
                        .memberId(member.getMemberId())
                        .userKey(memberBankResponse.getUserKey())
                        .build()
        );

        return LoginResponse.createLoginResponse(accessToken);
    }

    /*
        금융 API
    */
    // 회원가입 API
    private MemberBankResponse createMemberBank(CreatedRequest createdRequest) throws JsonProcessingException {

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
        return objectMapper.readValue(response.getBody(), MemberBankResponse.class);

    }

    // 계좌 생성 API
    private String createMemberAccount(MemberBankResponse memberBankResponse) throws JsonProcessingException {
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

    // 로그인 API
    private MemberBankResponse loginMemberBank(Member member) throws JsonProcessingException {
        // 1. body 객체 생성
        HashMap<String, String> body = new HashMap<>();
        body.put("apiKey", apiKey);
        body.put("userId", member.getEmail());

        // 2. HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 객체 생성
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 4. 외부 API 호출
        String url = "/member/search";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // 5. 외부 API 응답 처리
        log.info("로그인 API 결과: " + response.getBody());
        return objectMapper.readValue(response.getBody(), MemberBankResponse.class);

    }
}
