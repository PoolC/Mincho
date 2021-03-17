package org.poolc.api.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.poolc.api.auth.exception.UnauthorizedException;

import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    private Member unacceptedMember;
    private Member member;
    private Member admin;

    @BeforeEach
    void setUp() {
        unacceptedMember = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("UNACCEPTED_MEMBER_ID")
                .passwordHash("UNACCEPTED_MEMBER_PASSWORD")
                .email("example@email.com")
                .phoneNumber("010-4444-4444")
                .name("UNACCEPTED_MEMBER_NAME")
                .department("exampleDepartment")
                .studentID("2021147598")
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .profileImageURL(null)
                .introduction("난 아직 인증된 회원이 아니지")
                .isExcepted(false)
                .roles(new HashSet<>() {{
                    add(MemberRole.UNACCEPTED);
                }})
                .build();

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
                .roles(new HashSet<>() {{
                    add(MemberRole.MEMBER);
                }})
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
                .roles(new HashSet<>() {{
                    add(MemberRole.MEMBER);
                    add(MemberRole.ADMIN);
                }})
                .build();
    }

    @Test
    void toggleAdmin() {
        admin.toggleAdmin(member);

        assertThat(member.isAdmin()).isTrue();
    }

    @Test
    void nonAdminCannotToggleAdmin() {
        assertThatThrownBy(() -> member.toggleAdmin(admin))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Only admins");
    }

    @Test
    void cannotToggleUnacceptedMember() {
        assertThatThrownBy(() -> admin.toggleAdmin(unacceptedMember))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("is not a member yet");
    }

    @Test
    void 멤버퇴출() {
        admin.updateStatus(member, "EXPELLED");
        assertThat(member.getStatus()).isEqualTo("EXPELLED");
    }

    @Test
    void 관리자멤버퇴출() {
        admin.toggleAdmin(member);
        admin.updateStatus(member, "EXPELLED");
        assertThat(member.getStatus()).isEqualTo("EXPELLED");
        assertThat(member.isAdmin()).isEqualTo(false);
    }

    @Test
    void 졸업회원() {
        admin.toggleAdmin(member);
        admin.updateStatus(member, MemberRole.GRADUATED.name());
        assertThat(member.getStatus()).isEqualTo("GRADUATED");
        assertThat(member.isAdmin()).isEqualTo(false);
    }

    @Test
    void Excepted시키기() {
        assertThat(member.getIsExcepted()).isEqualTo(false);
        admin.Except(member);
        assertThat(member.getIsExcepted()).isEqualTo(true);
    }

    @Test
    void 동시에_있을_수_없는_역할이_부여됨() {
        assertThatThrownBy(() -> Member.builder()
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
                .roles(new HashSet<>() {{
                    add(MemberRole.UNACCEPTED);
                    add(MemberRole.MEMBER);
                }})
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MemberRole.UNACCEPTED.name());
    }


    @Test
    void 회장인데_비회원일_수_없음() {
        assertThatThrownBy(() -> Member.builder()
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
                .roles(new HashSet<>() {{
                    add(MemberRole.ADMIN);
                }})
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContainingAll(MemberRole.ADMIN.name(), "MEMBER");
    }
}