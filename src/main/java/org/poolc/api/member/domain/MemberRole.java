package org.poolc.api.member.domain;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public enum MemberRole {
    // enum 순서 중요함. 밑으로 갈수록 높은 권한으로 가정
    PUBLIC(false, true, false, false, false, "외부인", Collections.emptyList()),
    QUIT(false, true, false, true, false, "자진탈퇴", Collections.emptyList()),
    EXPELLED(false, true, false, false, false, "자격상실", Collections.emptyList()),
    UNACCEPTED(false, true, false, false, false, "승인 전", Collections.emptyList()),
    MEMBER(false, false, true, false, false, "일반회원", Collections.emptyList()),
    INACTIVE(false, false, true, true, false, "한 학기 비활동", Collections.singletonList(MemberRole.MEMBER)),
    COMPLETE(false, false, true, true, false, "수료회원", Collections.singletonList(MemberRole.MEMBER)),
    GRADUATED(false, false, true, true, false, "졸업회원", Collections.singletonList(MemberRole.MEMBER)),
    ADMIN(true, false, true, true, false, "임원진", Collections.singletonList(MemberRole.MEMBER)),
    SUPER_ADMIN(true, true, true, false, false, "슈퍼 관리자", Collections.singletonList(MemberRole.MEMBER));

    private final boolean admin;
    private final boolean hideInfo;
    private final boolean member;
    private final boolean selfToggleable;
    private final boolean onlyAdminToggleable;
    private final String description;
    private final List<MemberRole> requiredRoles;

    MemberRole(boolean admin, boolean hideInfo, boolean member, boolean selfToggleable, boolean onlyAdminToggleable, String description, List<MemberRole> requiredRoles) {
        this.admin = admin;
        this.hideInfo = hideInfo;
        this.member = member;
        this.selfToggleable = selfToggleable;
        this.onlyAdminToggleable = onlyAdminToggleable;
        this.description = description;
        this.requiredRoles = requiredRoles;
    }
}
