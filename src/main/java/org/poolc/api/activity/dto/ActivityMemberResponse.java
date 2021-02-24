package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.activity.domain.ActivityMember;

@Getter
public class ActivityMemberResponse {
    private final String name;
    private final Long id;

    @JsonCreator
    public ActivityMemberResponse(ActivityMember member) {
        this.name = member.getMember().getName();
        this.id = member.getId();
    }
}
