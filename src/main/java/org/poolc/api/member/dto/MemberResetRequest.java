package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class MemberResetRequest {
    private final String email;
    private final String passwordResetToken;
    private final String newPassword;
    private final String newPasswordCheck;

    @JsonCreator
    public MemberResetRequest(String email, String passwordResetToken, String newPassword, String newPasswordCheck) {
        this.email = email;
        this.passwordResetToken = passwordResetToken;
        this.newPassword = newPassword;
        this.newPasswordCheck = newPasswordCheck;
    }
}
