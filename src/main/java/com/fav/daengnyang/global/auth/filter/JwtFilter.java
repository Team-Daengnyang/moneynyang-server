package com.fav.daengnyang.global.auth.filter;

import com.fav.daengnyang.domain.member.entity.Member;
import com.fav.daengnyang.domain.member.service.MemberService;
import com.fav.daengnyang.global.auth.dto.MemberPrincipal;
import com.fav.daengnyang.global.auth.utils.JWTProvider;
import com.fav.daengnyang.global.exception.CustomException;
import com.fav.daengnyang.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends GenericFilterBean{

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JWTProvider jwtProvider;
    private final MemberService memberService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logger.info("[JwtFilter] : " + request.getRequestURL().toString());

        // 로그인, 회원가입은 필터를 적용하지 않음
        String requestURI = request.getRequestURI();
        logger.info("requestURI : " + requestURI);
        if (requestURI.equals("/api/v1/members") || requestURI.equals("/api/v1/members/login")) {
            filterChain.doFilter(request, servletResponse);
            return;
        }

        String jwt = resolveToken(request);

        if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
            Long memberId = jwtProvider.getMemberId(jwt);
            String userKey = jwtProvider.getUserKey(jwt);
            log.info("0");
            Member member = memberService.findByMemberId(memberId);

            log.info("1");
            UserDetails userDetails = MemberPrincipal.createMemberAuthority(member, userKey);
            log.info("3");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else{
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        filterChain.doFilter(request, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
