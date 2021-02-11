package org.poolc.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class AuthResponse {
    private final String accessToken;

    @JsonCreator
    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
