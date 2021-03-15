package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

@Getter
public class MemberResponseWithHour {

    private final MemberResponse member;
    private final Long hour;
    private final Boolean isExcepted;
    
    @JsonCreator
    public MemberResponseWithHour(MemberResponse member, Long hour, Boolean isExcepted) {
        this.member = member;
        this.hour = hour;
        this.isExcepted = isExcepted;
    }

    public static MemberResponseWithHour of(Member member, Long hour) {
        return new MemberResponseWithHour(MemberResponse.of(member), hour, member.getIsExcepted());
    }
}
