package com.fav.daengnyang.global.auth.utils;

import com.fav.daengnyang.global.auth.dto.MemberAuthority;
import com.fav.daengnyang.global.auth.exception.InvalidJWTException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class JWTProvider implements InitializingBean {

    // 토큰 만료 시간 : 6시간
    private static final int ACCESS_TOKEN_EXPIRATION_PERIOD  = 60 * 60 * 6;

    // JWT 키 값
    private static final String USER_KEY = "userKey";
    private static final String USER_ID = "userId";

    // 외부 설정 파일에서 주입되는 JWT 키 값
    @Value("${jwt.key}")
    private String encodedKeyValue;

    // JWT 서명에 사용되는 SecretKey 객체
    private SecretKey key;

    // 현재 시간 제공하는 Clock 객체
    private final Clock clock;

    // Bean 초기화 후 SecretKey 설정
    @Override
    public void afterPropertiesSet() throws Exception {
        key = buildKey();
    }

    // 디코딩하여 SecretKey를 생성하는 메서드
    private SecretKey buildKey(){
        byte[] decodedKeyValue = Base64.getDecoder().decode(encodedKeyValue);
        return Keys.hmacShaKeyFor(decodedKeyValue);
    }

    // JWT 토큰 생성
    public String buildAccessToken(MemberAuthority authority){
        // 현재 시각
        Instant now = clock.instant();

        return Jwts.builder()
                .claim(USER_KEY, authority.getUserKey())
                .claim(USER_ID, authority.getMemberId())
                .signWith(key)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ACCESS_TOKEN_EXPIRATION_PERIOD)))
                .compact();
    }

    // JWT 토큰 파싱 후 클레임 반환
    private Claims parsePayload(String token){
        Claims payload;
        try{
            // JWT 파싱 후 유효성 검증
            payload = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setClock(() -> Date.from(clock.instant()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException exception) {
            throw new InvalidJWTException("만료된 token 입니다.", exception);
        } catch (JwtException exception) {
            throw new InvalidJWTException("유효하지 않은 token 입니다.", exception);
        }

        return payload;
    }

    @SuppressWarnings("unchecked")
    private <T> T parsePayload(Object raw, Class<T> targetClass, String key){
        if(raw == null){
            throw new IllegalArgumentException(String.format("token에 %s가 존재하지 않습니다.", key));
        }
        return (T) raw;
    }

    public MemberAuthority parseAccessToken(String token){
        Claims payload = parsePayload(token);

        String userKey = parsePayload(payload.get(USER_KEY), String.class, USER_KEY);
        Long userId = parsePayload(payload.get(USER_ID), Long.class, USER_ID);

        return MemberAuthority.createMemberAuthority(userKey, userId);
    }
}
