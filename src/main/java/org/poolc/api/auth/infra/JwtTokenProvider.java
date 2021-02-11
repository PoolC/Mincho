package org.poolc.api.auth.infra;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.poolc.api.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey = "adf";
    @Value("${security.jwt.token.expire-length}")
    private long expireTimeInMS = 3600000;

    public String createToken(Member member) {
        Date now = new Date();

        return Jwts.builder()
                .setIssuer("PoolC/PROJECT_NAME_HERE")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTimeInMS))
                .setClaims(Jwts
                        .claims()
                        .setSubject(member.getUUID())
                        .setSubject(member.getIsAdmin().toString()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
