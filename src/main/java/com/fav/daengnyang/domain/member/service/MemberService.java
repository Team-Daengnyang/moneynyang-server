package com.fav.daengnyang.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import com.fav.daengnyang.domain.member.service.dto.request.AccountCreationHeaderRequest;
import com.fav.daengnyang.domain.member.service.dto.request.CreatedRequest;
import com.fav.daengnyang.domain.member.service.dto.response.AccountCreationResponse;
import com.fav.daengnyang.domain.member.service.dto.response.LoginResponse;
import com.fav.daengnyang.domain.member.service.dto.response.MemberBankResponse;
import com.fav.daengnyang.domain.member.service.dto.response.MemberInfoResponse;
import com.fav.daengnyang.domain.pet.service.dto.response.CheckResponse;
import com.fav.daengnyang.domain.target.repository.TargetRepository;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.auth.utils.JWTProvider;
import com.fav.daengnyang.global.exception.CustomException;
import com.fav.daengnyang.global.exception.ErrorCode;
import com.fav.daengnyang.global.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final TargetRepository targetRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;

    private final TransactionService transactionService;

    @Value("${api.key}")
    private String apiKey;

    public LoginResponse createMember(CreatedRequest createdRequest) throws JsonProcessingException {
        // 1. 금융 API 회원가입
        MemberBankResponse memberBankResponse = createMemberBank(createdRequest);
        System.out.println("memberBankResponse: " + memberBankResponse);
        // 2. 예금 계좌 개설
        String depositAccount = createMemberAccount(memberBankResponse);
        // 3. 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(createdRequest.getPassword());
        // 4. DB에 회원 정보 저장
        Member member = save(createdRequest, depositAccount, encodedPassword);
        // 5. 송금하기 Test
        transactionService.makeDeposit(depositAccount, memberBankResponse.getUserKey());
        // 6. accessToken 생성
        String accessToken = jwtProvider.buildAccessToken(memberBankResponse.getUserKey(), member.getMemberId());
        // 7. LoginResponse DTO 생성
        return LoginResponse.createLoginResponse(accessToken);
    }

    // Member 저장
    private Member save(CreatedRequest createdRequest, String depositAccount, String encodedPassword){
        Member member = Member.createMember(createdRequest, depositAccount, encodedPassword);
        return memberRepository.save(member);
    }

    // memberId로 DB에서 정보 가져오기
    public Member findByMemberId(Long id) {
        return memberRepository.findByMemberId(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 이메일 중복 체크
    public CheckResponse checkDuplicateEmail(String email) throws JsonProcessingException {
        //금융 API 회원가입
        return checkMemberBank(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            // 1. 사용자 확인
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            // 2. 금융 API 연결
            MemberBankResponse memberBankResponse = loginMemberBank(member);
            return MemberPrincipal.createMemberAuthority(member, memberBankResponse.getUserKey());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
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

    public MemberInfoResponse getMemberInfo(Long memberId) {
        // 2. Member 가져오기
        Member member = findByMemberId(memberId);
        // 3. 서비스 이용 시작일과 현재일의 차이 계산
        Long daysUsingService = Duration.between(member.getCreated(), LocalDateTime.now()).toDays();
        daysUsingService = daysUsingService+1;
        // 4. 현재 설정된 타겟의 총 개수 구하기
        int totalTargets = targetRepository.findAllByMemberId(memberId).size();
        // 5. 5일당 레벨 1씩 계산
        int level = (int) (daysUsingService / 5) + 1;
        // 6. MemberInfoResponse 생성 및 반환
        return MemberInfoResponse.builder()
                .memberId(member.getMemberId())
                .memberName(member.getName())
                .memberLevel(level)
                .memberDate(daysUsingService)
                .memberTarget(totalTargets)
                .build();
    }

    private CheckResponse checkMemberBank(String email) throws JsonProcessingException {
        // 1. body 객체 생성
        HashMap<String, String> body = new HashMap<>();
        body.put("apiKey", apiKey);
        body.put("userId", email);

        // 2. HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. HttpEntity 객체 생성
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 4. 외부 API 호출
        String url = "/member/search";
        try{
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("아이디 중복체크" + response.getBody());

            return CheckResponse.createCheckResponse("이미 존재하는 이메일입니다.");

        } catch (HttpClientErrorException e) {
            log.info("아이디 중복체크" + e.getResponseBodyAsString());
           CheckResponse checkResponse = objectMapper.readValue(e.getResponseBodyAsString(), CheckResponse.class);
           if(checkResponse.getResponseCode().equals("E4003")){
               checkResponse.updateCheckResponse("사용가능한 이메일입니다.");
           }
           return checkResponse;
        }
    }
}
