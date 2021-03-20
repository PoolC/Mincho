package org.poolc.api.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.poolc.api.auth.exception.UnauthorizedException;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class MemberRolesTest {
    private MemberRoles memberRoles;
    private MemberRoles adminRoles;
    private MemberRoles graduatedRoles;
    private MemberRoles superAdminRoles;
    private MemberRoles variousSpecialRoles;
    private MemberRoles variousNonMemberRoles;

    @BeforeEach
    void setUp() {
        memberRoles = new MemberRoles(Set.of(MemberRole.MEMBER));
        adminRoles = new MemberRoles(Set.of(MemberRole.MEMBER, MemberRole.ADMIN));
        graduatedRoles = new MemberRoles(Set.of(MemberRole.MEMBER, MemberRole.GRADUATED));
        superAdminRoles = new MemberRoles(Set.of(MemberRole.MEMBER, MemberRole.SUPER_ADMIN));
        variousSpecialRoles = new MemberRoles(Set.of(MemberRole.MEMBER, MemberRole.GRADUATED, MemberRole.ADMIN));
        variousNonMemberRoles = new MemberRoles(Set.of(MemberRole.EXPELLED, MemberRole.QUIT, MemberRole.UNACCEPTED));
    }

    @Test
    void testAdd() {
        memberRoles.add(MemberRole.ADMIN);
        assertThat(memberRoles.getHighestRole()).isEqualTo(MemberRole.ADMIN);
    }

    @ParameterizedTest
    @ValueSource(strings = {"PUBLIC", "EXPELLED", "QUIT", "UNACCEPTED"})
    void testAddNonMemberRoleOnOnlyMemberRoles(String roleNames) {
        MemberRole role = MemberRole.valueOf(roleNames);

        memberRoles.add(role);
        assertThat(memberRoles.getHighestRole()).isEqualTo(role);
    }

    @DisplayName("특수 role을 갖고 있으면 특수 role 먼저 삭제한 후에 non member role 삽입해야됨")
    @Test
    void cannotAddNonMemberRoleWhenThereAreVariousMemberRoles() {
        assertThatIllegalArgumentException().isThrownBy(() -> variousSpecialRoles.add(MemberRole.QUIT));
        assertThatIllegalArgumentException().isThrownBy(() -> adminRoles.add(MemberRole.QUIT));

        variousSpecialRoles.delete(MemberRole.ADMIN);
        assertThatIllegalArgumentException().isThrownBy(() -> variousSpecialRoles.add(MemberRole.QUIT));

        variousSpecialRoles.delete(MemberRole.GRADUATED);
        variousSpecialRoles.add(MemberRole.QUIT);
        assertThat(variousSpecialRoles.getHighestRole()).isEqualTo(MemberRole.QUIT);
    }

    @Test
    void cannotAddNonMemberRoleOnSpecialRoles() {
        assertThatIllegalArgumentException().isThrownBy(() -> graduatedRoles.add(MemberRole.QUIT));
        assertThatIllegalArgumentException().isThrownBy(() -> adminRoles.add(MemberRole.EXPELLED));
    }

    @Test
    void addMemberOnNonMemberRolesOnly() {
        variousNonMemberRoles.add(MemberRole.MEMBER);
        assertThat(variousNonMemberRoles.getHighestRole()).isEqualTo(MemberRole.MEMBER);
        assertThat(variousNonMemberRoles.getAuthorities()).hasSize(1);
    }

    @Test
    void testMultipleAddsStillAddsOnce() {
        memberRoles.add(MemberRole.ADMIN);
        memberRoles.add(MemberRole.ADMIN);
        memberRoles.add(MemberRole.ADMIN);
        assertThat(memberRoles.getHighestRole()).isEqualTo(MemberRole.ADMIN);

        memberRoles.delete(MemberRole.ADMIN);
        assertThat(memberRoles.getHighestRole()).isEqualTo(MemberRole.MEMBER);
    }

    @Test
    void superAdminCannotBeAddedOrDeleted() {
        assertThatThrownBy(() -> memberRoles.add(MemberRole.SUPER_ADMIN)).isInstanceOf(UnauthorizedException.class);
        assertThatThrownBy(() -> adminRoles.add(MemberRole.SUPER_ADMIN)).isInstanceOf(UnauthorizedException.class);
        assertThatThrownBy(() -> superAdminRoles.delete(MemberRole.SUPER_ADMIN)).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void testDelete() {
        memberRoles.delete(MemberRole.MEMBER);
        assertThat(memberRoles.getHighestRole()).isEqualTo(MemberRole.PUBLIC);

        adminRoles.delete(MemberRole.ADMIN);
        assertThat(adminRoles.getHighestRole()).isEqualTo(MemberRole.MEMBER);

        variousSpecialRoles.delete(MemberRole.GRADUATED);
        assertThat(variousSpecialRoles.getHighestRole()).isEqualTo(MemberRole.ADMIN);
        assertThat(variousSpecialRoles.getAuthorities()).hasSize(2);
    }

    @Test
    void cannotDeleteMemberRoleIfSpecialRoleExists() {
        assertThatIllegalArgumentException().isThrownBy(() -> variousSpecialRoles.delete(MemberRole.MEMBER));
        assertThatIllegalArgumentException().isThrownBy(() -> graduatedRoles.delete(MemberRole.MEMBER));
    }

    @Test
    void deleteNonExistingRoleDoesNothing() {
        variousSpecialRoles.delete(MemberRole.COMPLETE);
        assertThat(variousSpecialRoles.getAuthorities()).hasSize(3);
    }

    @Test
    void testHighestRole() {
        assertThat(variousSpecialRoles.getHighestRole()).isEqualTo(MemberRole.ADMIN);
        assertThat(graduatedRoles.getHighestRole()).isEqualTo(MemberRole.GRADUATED);
    }

    @Test
    void invalidRolesDoesNotCreate() {
        assertThatIllegalStateException().isThrownBy(() -> new MemberRoles(Set.of(MemberRole.ADMIN)));
        assertThatIllegalStateException().isThrownBy(() -> new MemberRoles(Set.of(MemberRole.GRADUATED)));
    }

    @Test
    void testGetDefault() {
        MemberRoles roles;

        roles = MemberRoles.getDefaultFor(MemberRole.UNACCEPTED);
        assertThat(roles.getAuthorities()).hasSize(1);

        roles = MemberRoles.getDefaultFor(MemberRole.MEMBER);
        assertThat(roles.getAuthorities()).hasSize(1);

        roles = MemberRoles.getDefaultFor(MemberRole.GRADUATED);
        assertThat(roles.getAuthorities()).hasSize(2);

        roles = MemberRoles.getDefaultFor(MemberRole.SUPER_ADMIN);
        assertThat(roles.getAuthorities()).hasSize(2);
    }
}
