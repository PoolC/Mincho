package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

@Getter
public class HostResponse {

    private final String loginID;
    private final String name;

    @JsonCreator
    public HostResponse(String loginID, String name) {
        this.loginID = loginID;
        this.name = name;
    }

    public static HostResponse of(Member member) {
        return new HostResponse(member.getLoginID(), member.getName());
    }
}