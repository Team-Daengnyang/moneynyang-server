package com.fav.daengnyang.global.auth.utils;

import com.fav.daengnyang.global.auth.exception.InvalidJWTException;
import io.jsonwebtoken.*;
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
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class JWTProvider implements InitializingBean {

    // 토큰 만료 시간 : 6시간
    private static final int ACCESS_TOKEN_EXPIRATION_PERIOD  = 60 * 60 * 100;

    // JWT 키 값
    private static final String USER_KEY = "userKey";
    private static final String MEMBER_ID = "memberId";

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
    public String buildAccessToken(String userKey, Long memberId){
        // 현재 시각
        Instant now = clock.instant();

        return Jwts.builder()
                .claim(USER_KEY, userKey)
                .claim(MEMBER_ID, memberId)
                .signWith(key)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ACCESS_TOKEN_EXPIRATION_PERIOD)))
                .compact();
    }

    public boolean validateToken(String token){
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

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
            throw new InvalidJWTException("유효하지 않은 token 입니다.", exception);
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

    public String getUserKey(String token) {
        Claims payload = parsePayload(token);
        return parsePayload(payload.get(USER_KEY), String.class, USER_KEY);
    }

    public Long getMemberId(String token) {
        // 1. token을 payload로 파싱
        Claims payload = parsePayload(token);
        // 2. MEMBER_ID를 Number 타입으로 받음 (ClassCastException 발생 제어)
        Number memberId = (Number) payload.get(MEMBER_ID);
        // 3. Long 타입으로 변환 후 리턴
        return memberId.longValue();
    }

}
