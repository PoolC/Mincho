package org.poolc.api.auth.infra;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.auth.vo.ParsedTokenValues;
import org.poolc.api.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long expireTimeInMS;

    public String createToken(Member member) {
        Date now = new Date();
        return Jwts.builder()
                .claim("id", member.getUUID())
                .claim("isAdmin", member.getIsAdmin().toString())
                .setIssuedAt(now)
                .setIssuer("Poolc/PROJECT_NAME_HERE")
                .setExpiration(new Date(now.getTime() + expireTimeInMS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public ParsedTokenValues getUserInfo(String token) {
        Claims body = tryToGetTokenClaims(token);
        return new ParsedTokenValues(body);
    }

    public void validateToken(String token) {
        tryToGetTokenClaims(token);
    }

    private Claims tryToGetTokenClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthenticatedException("Invalid token\n" + e.getMessage());
        }
    }
}