package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.dto.MemberResponse;

@Getter
public class AttendanceResponse {
    private final MemberResponse member;
    private final Boolean attended;

    @JsonCreator
    public AttendanceResponse(Member member, Boolean attended) {
        this.member = MemberResponse.of(member);
        this.attended = attended;
    }
}
