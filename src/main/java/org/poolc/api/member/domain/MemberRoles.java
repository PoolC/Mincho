package org.poolc.api.member.domain;

import org.poolc.api.auth.exception.UnauthorizedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@Embeddable
public class MemberRoles {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "member_uuid"))
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new HashSet<>();

    protected MemberRoles() {
    }

    public MemberRoles(Set<MemberRole> roles) {
        this.roles = new HashSet<>(roles);

        checkRolesAreCorrect();
    }

    public static MemberRoles getDefaultFor(MemberRole role) {
        Set<MemberRole> returnRoles = new HashSet<>();

        returnRoles.add(role);
        returnRoles.addAll(role.getRequiredRoles());

        return new MemberRoles(returnRoles);
    }

    public boolean hasRole(MemberRole role) {
        return roles.contains(role);
    }

    public MemberRole getHighestRole() {
        return Stream.of(MemberRole.values())
                .filter(roles::contains)
                .findFirst()
                .orElse(MemberRole.PUBLIC);
    }

    public boolean isAcceptedMember() {
        return !roles.contains(MemberRole.UNACCEPTED);
    }

    public boolean isExpelled() {
        return !roles.contains(MemberRole.EXPELLED);
    }

    public boolean isMember() {
        return roles.contains(MemberRole.MEMBER);
    }

    public boolean isAdmin() {
        return roles.stream()
                .anyMatch(MemberRole::isAdmin);
    }

    public void changeRole(MemberRole role) {
        if (role.equals(MemberRole.SUPER_ADMIN)) {
            throw new UnauthorizedException("Usage of super admin is prohibited");
        }

        roles.clear();
        roles.add(role);
        roles.addAll(role.getRequiredRoles());
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(MemberRole::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public boolean checkIsExcepted() {
        int memberOrdinal = MemberRole.MEMBER.ordinal();
        if (getHighestRole().ordinal() < memberOrdinal) {
            return true;
        }
        return false;
    }

    private void checkRolesAreCorrect() {
        checkIsMemberButHasNonMemberRole();
        checkIsSpecialRoleButDoesNotHaveMemberRole();
    }

    private void checkIsMemberButHasNonMemberRole() {
        if (!roles.contains(MemberRole.MEMBER)) {
            return;
        }

        roles.stream()
                .filter(not(MemberRole::isMember))
                .findAny()
                .ifPresent(role -> {
                    throw new IllegalStateException("Member cannot have non-member role: " + role.name());
                });
    }

    private void checkIsSpecialRoleButDoesNotHaveMemberRole() {
        if (roles.contains(MemberRole.MEMBER)) {
            return;
        }

        roles.stream()
                .filter(not(MemberRole.MEMBER::equals))
                .filter(MemberRole::isMember)
                .findAny()
                .ifPresent(specialRole -> {
                    throw new IllegalStateException(
                            String.format("Special role %s also needs %s role",
                                    specialRole.name(), MemberRole.MEMBER));
                });
    }
}
