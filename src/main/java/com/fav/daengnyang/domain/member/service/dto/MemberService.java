package com.fav.daengnyang.domain.member.service.dto;

import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    // Member 저장
    public Member save(Member member){
        return memberRepository.save(member);
    }

    /*
        금융 API
    */
    // 회원가입 API
    public CreateMemberBankResponseDto createMemberBank(Member member){
        //String url = "/member";

    }
}
