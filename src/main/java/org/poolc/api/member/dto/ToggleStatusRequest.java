package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class ToggleStatusRequest {
    private final String status;

    @JsonCreator
    public ToggleStatusRequest(String status) {
        this.status = status;
    }
}
