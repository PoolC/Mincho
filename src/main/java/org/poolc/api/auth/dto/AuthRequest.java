package org.poolc.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class AuthRequest {
    private final String loginID;
    private final String password;

    @JsonCreator
    public AuthRequest(String loginID, String password) {
        this.loginID = loginID;
        this.password = password;
    }
}
