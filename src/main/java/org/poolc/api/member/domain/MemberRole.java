package org.poolc.api.member.domain;

import lombok.Getter;

@Getter
public enum MemberRole {
    PUBLIC("외부인"),
    UNACCEPTED("관리자 승인 전"),
    EXPELLED("자격 상실"),
    MEMBER("일반 회원"),
    INACTIVE("한 학기 비활동"),
    COMPLETE("수료 회원"),
    GRADUATED("졸업 회원"),
    ADMIN("임원진");

    private final String description;

    MemberRole(String description) {
        this.description = description;
    }
}
