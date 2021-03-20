package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class ToggleRoleRequest {
    private final String role;

    @JsonCreator
    public ToggleRoleRequest(String role) {
        this.role = role;
    }
}
