package org.poolc.api.member.domain;

import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Getter
public enum MemberRole {
    SUPER_ADMIN("슈퍼 계정", true, true, true),
    ADMIN("임원진", true, false, true),
    GRADUATED("졸업 회원", true, false, false),
    COMPLETE("수료 회원", true, false, false),
    INACTIVE("한 학기 비활동", true, false, false),
    MEMBER("일반 회원", true, false, false),
    UNACCEPTED("관리자 승인 전", false, true, false),
    EXPELLED("자격 상실", false, true, false),
    PUBLIC("외부인", false, true, false);

    private final String description;
    private final boolean member;
    private final boolean hideInfo;
    private final boolean admin;

    MemberRole(String description, boolean member, boolean hideInfo, boolean admin) {
        this.description = description;
        this.member = member;
        this.hideInfo = hideInfo;
        this.admin = admin;
    }

    public static MemberRole getHighestStatus(Collection<MemberRole> roles) {
        return Stream.of(values())
                .filter(roles::contains)
                .findFirst()
                .orElse(PUBLIC);
    }

    public static Set<MemberRole> getRolesOf(MemberRole newRole) {
        Set<MemberRole> result = new HashSet<>();

        result.add(newRole);
        if (newRole.isMember() && !newRole.equals(MEMBER)) {
            result.add(MEMBER);
        }

        return result;
    }
}
