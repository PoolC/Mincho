package org.poolc.api.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.MemberRole;

@RequiredArgsConstructor
@Getter
public class MemberRolesResponse {
    private final String name;
    private final String description;

    public static MemberRolesResponse of(MemberRole role) {
        return new MemberRolesResponse(role.name(), role.getDescription());
    }
}
