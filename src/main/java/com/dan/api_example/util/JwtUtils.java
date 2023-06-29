package com.dan.api_example.util;

import com.dan.api_example.common.exception.BaseException;
import com.dan.api_example.common.response.BaseResponseStatus;
import com.dan.api_example.entity.User;
import com.dan.api_example.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

@Component
public class JwtUtils {

    private final UserRepository userRepository;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 1주일

    private final Key key;

    public JwtUtils(@Value("${jwt.secret}") String jwtSecret, UserRepository userRepository) {
        this.userRepository = userRepository;
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, String> generateToken(Long userId, String role) {

        String accessToken = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("uid", userId)
                .setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Map<String, String> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", accessToken);
        tokenInfo.put("refreshToken", refreshToken);

        return tokenInfo;
    }

    public String getJwtHeader() {
        HttpServletRequest request
                = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    public String getJwt() throws BaseException{
        String accessToken = getJwtHeader();

        if (accessToken == null) {
            throw new BaseException(BaseResponseStatus.NO_JWT);
        }
        return accessToken;
    }

    public Long getUserId(String accessToken) throws BaseException{
        try {
            Long userId = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .get("uid", Long.class);
            return userId;
        } catch (ExpiredJwtException expiredJwt) {
            throw new BaseException(BaseResponseStatus.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
        }
    }

    public Authentication getAuthentication(String accessToken) throws BaseException {
        User user = userRepository.findById(getUserId(accessToken)).orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return new UsernamePasswordAuthenticationToken(getUserId(accessToken), "", authorities);
    }

    public Long getUserIdV2() {
        String jwt = getJwt();
        return getUserId(jwt);
    }
}
