package org.poolc.api.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class MemberRolesTest {
    private MemberRoles graduatedRoles;
    private MemberRoles variousSpecialRoles;

    @BeforeEach
    void setUp() {
        graduatedRoles = new MemberRoles(Set.of(MemberRole.MEMBER, MemberRole.GRADUATED));
        variousSpecialRoles = new MemberRoles(Set.of(MemberRole.MEMBER, MemberRole.GRADUATED, MemberRole.ADMIN));
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
