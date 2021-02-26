package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.activity.domain.ActivityMember;

@Getter
public class ActivityMemberResponse {
    private final String name;
    private final Long id;

    @JsonCreator
    public ActivityMemberResponse(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public static ActivityMemberResponse of(ActivityMember member) {
        return new ActivityMemberResponse(member.getMember().getName(), member.getId());
    }
}
