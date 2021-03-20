package org.poolc.api.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.poolc.api.auth.exception.UnauthorizedException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MemberTest {
    private Member unacceptedMember;
    private Member member;
    private Member admin;
    private Member superAdmin;

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
                .roles(MemberRoles.getDefaultFor(MemberRole.UNACCEPTED))
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

        superAdmin = Member.builder()
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
                .roles(MemberRoles.getDefaultFor(MemberRole.SUPER_ADMIN))
                .build();
    }

    @Test
    void testShouldHide() {
        assertThat(unacceptedMember.shouldHide()).isTrue();
        assertThat(member.shouldHide()).isFalse();
        assertThat(admin.shouldHide()).isFalse();
        assertThat(superAdmin.shouldHide()).isTrue();
    }

    @Test
    void adminIsAlsoMember() {
        assertThat(admin.isMember()).isTrue();
        assertThat(admin.isAdmin()).isTrue();
    }

    @Test
    void testGetStatus() {
        assertThat(unacceptedMember.getRole()).isEqualTo("UNACCEPTED");
        assertThat(member.getRole()).isEqualTo("MEMBER");
        assertThat(admin.getRole()).isEqualTo("ADMIN");
        assertThat(superAdmin.getRole()).isEqualTo("SUPER_ADMIN");
    }

    @Test
    void testAcceptMember() {
        unacceptedMember.acceptMember();
        assertThat(unacceptedMember.isMember()).isTrue();
    }

    @Test
    void adminChangeRole() {
        admin.changeRole(member, MemberRole.GRADUATED);
        assertThat(member.getRole()).isEqualTo(MemberRole.GRADUATED.name());
    }

    @Test
    void selfChangeRole() {
        member.selfChangeRole(MemberRole.GRADUATED);
        assertThat(member.getRole()).isEqualTo(MemberRole.GRADUATED.name());
    }

    @Test
    void selfChangeToAdminIsUnauthorized() {
        assertThatThrownBy(() -> member.selfChangeRole(MemberRole.ADMIN))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void cannotChangeToSuperAdmin() {
        assertThatThrownBy(() -> admin.changeRole(superAdmin, MemberRole.MEMBER))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void memberCanQuit() {
        member.quit();
        assertThat(member.isMember()).isFalse();
    }

    @Test
    void Excepted시키기() {
        admin.toggleExcept(member);

        assertThat(member.getIsExcepted()).isEqualTo(true);
    }
}
