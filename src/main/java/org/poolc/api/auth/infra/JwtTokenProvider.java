package org.poolc.api.auth.infra;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.poolc.api.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

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

    public HashMap<String, String> getUserInfo(String token) {
        Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        HashMap<String, String> info = new HashMap<>();
        info.put("id", body.get("id").toString());
        info.put("isAdmin", body.get("isAdmin").toString());
        return info;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            System.out.println(claims);
            if (claims.getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
