package org.poolc.api.activity.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityDomainTest {
    private Activity activity;
    private Member admin;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("MEMBER_ID")
                .passwordHash("MEMBER_PASSWORD")
                .email("example@email.com")
                .phoneNumber("010-4444-4444")
                .name("MEMBER_NAME")
                .department("exampleDepartment")
                .studentID("2021147598")
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .profileImageURL(null)
                .introduction("자기소개")
                .isExcepted(false)
                .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                .build();

        admin = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("ADMIN_ID")
                .passwordHash("ADMIN_PASSWORD")
                .email("example@email.com")
                .phoneNumber("010-4444-4444")
                .name("ADMIN_NAME")
                .department("exampleDepartment")
                .studentID("2021147599")
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .profileImageURL(null)
                .introduction("난 차기회장 이소정")
                .isExcepted(false)
                .roles(MemberRoles.getDefaultFor(MemberRole.ADMIN))
                .build();
        activity = Activity.builder()
                .id(1L)
                .title("기도 세미나")
                .description("도메인 테스트용")
                .host(admin)
                .startDate(LocalDate.now())
                .classHour("금요일 2시")
                .isSeminar(true)
                .capacity(2L)
                .hour(2L)
                .available(true)
                .tags(null)
                .sessions(null)
                .memberLoginIDs(new ArrayList<>())
                .build();
    }

    @Test
    void toggleApply() {
        activity.toggleApply(admin.getUUID());
        assertThat(activity.getMemberLoginIDs().get(0)).isEqualTo(admin.getUUID());
    }

    @Test
    void toggleApplyCancel() {
        activity.toggleApply(admin.getUUID());
        activity.toggleApply(admin.getUUID());
        assertThat(activity.getMemberLoginIDs().size()).isEqualTo(0);
    }

}
