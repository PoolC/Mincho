package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.activity.domain.ActivityMember;

@Getter
public class AttendanceCheckResponse {
    private final ActivityMemberResponse member;
    private final Boolean attended;

    @JsonCreator
    public AttendanceCheckResponse(ActivityMember member, Boolean attended) {
        this.member = ActivityMemberResponse.of(member);
        this.attended = attended;
    }
}
