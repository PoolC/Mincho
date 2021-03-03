package org.poolc.api.auth.infra;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final int AUTHORIZATION_CONSTRUCTION_LENGTH = 2;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long expireTimeInMS;

    public String createToken(Member member) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setIssuer("PoolC/Mincho")
                .setSubject(member.getUUID())
                .setExpiration(new Date(now.getTime() + expireTimeInMS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (isEmptyToken(token)) {
            return null;
        }

        return getPayloadIfBearerToken(token);
    }

    public String getSubject(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthenticatedException("Invalid token\n" + e.getMessage());
        }
    }

    private String getPayloadIfBearerToken(String token) {
        String[] typeAndCredentials = token.split(" ");

        checkIsBearerToken(typeAndCredentials);

        return typeAndCredentials[1];
    }

    private boolean isEmptyToken(String token) {
        return !StringUtils.hasText(token) || token.equals("Bearer");
    }

    private void checkIsBearerToken(String[] typeAndCredentials) {
        checkIsCorrectToken(typeAndCredentials.length);
        checkIsSupportedAuthorizationType(typeAndCredentials[0]);
    }

    private void checkIsCorrectToken(int typeAndCredentialsLength) {
        if (typeAndCredentialsLength != AUTHORIZATION_CONSTRUCTION_LENGTH) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private void checkIsSupportedAuthorizationType(String authorizationType) {
        if (!authorizationType.equals("Bearer")) {
            throw new JwtException("Authorization scheme not supported");
        }
    }
}