package com.fav.daengnyang.global.auth;

import com.fav.daengnyang.domain.member.service.dto.request.LoginRequest;
import com.fav.daengnyang.domain.member.service.dto.response.LoginResponse;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.auth.utils.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        MemberPrincipal memberPrincipal = (MemberPrincipal) authentication.getPrincipal();

        String accessToken = jwtProvider.buildAccessToken(memberPrincipal.getUserKey(), memberPrincipal.getMemberId());
        return LoginResponse.createLoginResponse(accessToken);
    }
}
