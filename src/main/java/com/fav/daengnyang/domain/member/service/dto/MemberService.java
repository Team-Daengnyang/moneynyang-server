package com.fav.daengnyang.domain.member.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.member.service.dto.request.CreatedRequest;
import com.fav.daengnyang.domain.member.service.dto.response.MemberBankResponse;
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

    // Member 저장
    public Member save(CreatedRequest createdRequest){

        Member member = Member.createMember(createdRequest);
        return memberRepository.save(member);
    }

    // accessToken 생성 -> created로 수정
    public LoginResponse createAccessToken(MemberBankResponse memberBankResponse){
        return LoginResponse.builder()
                .accessToken(
                        jwtProvider.buildAccessToken(MemberAuthority.builder()
                                .userId(memberBankResponse.getUserId())
                                .userKey(memberBankResponse.getUserKey())
                                .build())
                ).build();
    }

    /*
        금융 API
    */
    // 회원가입 API
    public MemberBankResponse createMemberBank(CreatedRequest createdRequest) throws JsonProcessingException {


        // 1. body 객체 생성
        log.debug("1. body 객체 생성 ");
        HashMap<String, String> body = new HashMap<>();
        body.put("apiKey", apiKey);
        body.put("userId", createdRequest.getEmail());

        // 2. HttpHeaders 설정
        log.debug("2. HttpHeaders 설정");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 객체 생성
        log.debug("// 3. HttpEntity 객체 생성");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 4. 외부 API 호출, rootUri를 config에 이미 선언
        log.debug("// 4. 외부 API 호출");
        String url = "/member";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        log.debug("회원 가입 API 결과: ", response);
        return objectMapper.readValue(response.getBody(), MemberBankResponse.class);
    }
}
