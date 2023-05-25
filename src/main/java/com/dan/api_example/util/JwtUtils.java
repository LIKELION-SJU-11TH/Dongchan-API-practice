package com.dan.api_example.util;

import com.dan.api_example.common.exception.BaseException;
import com.dan.api_example.common.response.BaseResponseStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtUtils {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 1주일

    private final Key key;

    public JwtUtils(@Value("${jwt.secret}") String jwtSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, String> generateToken(Long userId, String role) {
        int roleNo = 1;
        if (Objects.equals(role, "ROLE_ADMIN")) {
            roleNo = 99999;
        }

        String accessToken = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("uid", userId)
                .claim("uAuth", roleNo)
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

    public String getJwt() {
        HttpServletRequest request
                = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    public Long getUserId() throws BaseException{
        String accessToken = getJwt();

        if (accessToken == null) {
            throw new BaseException(BaseResponseStatus.NO_JWT);
        }

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

    public int getRoleNo() throws BaseException{
        String accessToken = getJwt();

        if (accessToken == null) {
            throw new BaseException(BaseResponseStatus.NO_JWT);
        }

        try {
            int roleNo = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .get("uAuth", Integer.class);
            return roleNo;
        } catch (ExpiredJwtException expiredJwt) {
            throw new BaseException(BaseResponseStatus.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
        }
    }
}
