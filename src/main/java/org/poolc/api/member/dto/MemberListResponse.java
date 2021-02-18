package org.poolc.api.member.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberListResponse {
    private final List<MemberResponse> data;

    @JsonCreator
    public MemberListResponse(List<MemberResponse> data) {
        this.data = data;
    }
}
