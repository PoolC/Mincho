package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdateMemberRequest {
    private final String name;
    private final String password;
    private final String passwordCheck;
    private final String email;
    private final String phoneNumber;

    @JsonCreator
    public UpdateMemberRequest(String name, String password, String passwordCheck, String email, String phoneNumber) {
        this.name = name;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
