package org.poolc.api.auth.vo;

import io.jsonwebtoken.Claims;
import lombok.Getter;

@Getter
public class ParsedTokenValues {
    private final String isAdmin;
    private final String UUID;

    public ParsedTokenValues(Claims claims) {
        this.isAdmin = claims.get("isAdmin").toString();
        this.UUID = claims.get("id").toString();
    }
}