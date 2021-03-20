package org.poolc.api.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.poolc.api.auth.exception.UnauthorizedException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    private Member unacceptedMember;
    private Member member;
    private Member member2;
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

        member2 = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("MEMBER_ID2")
                .passwordHash("MEMBER_PASSWORD2")
                .email("example2@email.com")
                .phoneNumber("010-1234-1234")
                .name("MEMBER_NAME2")
                .department("풀씨학과")
                .studentID("2021147591")
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
        assertThat(unacceptedMember.getStatus()).isEqualTo("UNACCEPTED");
        assertThat(member.getStatus()).isEqualTo("MEMBER");
        assertThat(admin.getStatus()).isEqualTo("ADMIN");
        assertThat(superAdmin.getStatus()).isEqualTo("SUPER_ADMIN");
    }

    @Test
    void testAcceptMember() {
        unacceptedMember.acceptMember();
        assertThat(unacceptedMember.isMember()).isTrue();
    }

    @Test
    void toggleAdmin() {
        admin.adminAddsRoleFor(member, MemberRole.ADMIN);

        assertThat(member.isAdmin()).isTrue();
    }

    @Test
    void superAdminCannotBeRevoked() {
        assertThatThrownBy(() -> admin.adminDeletesRoleFor(superAdmin, MemberRole.SUPER_ADMIN))
                .isInstanceOf(UnauthorizedException.class);
        assertThatThrownBy(() -> superAdmin.adminDeletesRoleFor(superAdmin, MemberRole.SUPER_ADMIN))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void cannotGrantSuperAdminRole() {
        assertThatThrownBy(() -> admin.adminAddsRoleFor(member, MemberRole.SUPER_ADMIN))
                .isInstanceOf(UnauthorizedException.class);
        assertThatThrownBy(() -> superAdmin.adminAddsRoleFor(admin, MemberRole.SUPER_ADMIN))
                .isInstanceOf(UnauthorizedException.class);
    }

    @ParameterizedTest
    @EnumSource(MemberRole.class)
    void nonAdminCannotToggleRoles(MemberRole role) {
        assertThatThrownBy(() -> member.adminAddsRoleFor(member2, role))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Only admins");

        assertThatThrownBy(() -> member.adminDeletesRoleFor(member2, role))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Only admins");
    }

    @ParameterizedTest
    @ValueSource(strings = {"INACTIVE", "COMPLETE", "GRADUATED"})
    void testSelfToggleRole(String roleName) {
        member.selfAddRole(MemberRole.valueOf(roleName));
        assertThat(member.getStatus()).isEqualTo(roleName);

        member.selfDeleteRole(MemberRole.valueOf(roleName));
        assertThat(member.getStatus()).isEqualTo("MEMBER");
    }

    @ParameterizedTest
    @ValueSource(strings = {"PUBLIC", "EXPELLED", "UNACCEPTED", "MEMBER", "SUPER_ADMIN"})
    void testCannotSelfDeleteNonToggleableRole(String roleName) {
        assertThatThrownBy(() -> member.selfAddRole(MemberRole.valueOf(roleName)))
                .isInstanceOf(UnauthorizedException.class);

        assertThatThrownBy(() -> member.selfDeleteRole(MemberRole.valueOf(roleName)))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void memberCanQuit() {
        member.quit();
        assertThat(member.isMember()).isFalse();
    }

    @Test
    void adminCanSelfAddAdminRole() {
        admin.selfAddRole(MemberRole.ADMIN);
        assertThat(admin.isAdmin()).isTrue();
    }

    @Test
    void adminCanSelfDeleteAdminRole() {
        admin.selfDeleteRole(MemberRole.ADMIN);
        assertThat(admin.isAdmin()).isFalse();
    }

    @Test
    void cannotGrantAdminToUnacceptedMember() {
        assertThatThrownBy(() -> admin.adminAddsRoleFor(unacceptedMember, MemberRole.ADMIN))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Excepted시키기() {
        admin.toggleExcept(member);

        assertThat(member.getIsExcepted()).isEqualTo(true);
    }
}
