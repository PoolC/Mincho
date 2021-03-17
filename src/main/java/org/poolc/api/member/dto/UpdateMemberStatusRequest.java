package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdateMemberStatusRequest {
    private final String loginId;
    private final String status;

    @JsonCreator
    public UpdateMemberStatusRequest(String loginId, String status) {
        this.loginId = loginId;
        this.status = status;
    }
}
