package org.poolc.api.member.domain;

import org.poolc.api.auth.exception.UnauthorizedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@Embeddable
public class MemberRoles {
    private final int ONLY_ONE_ELEMENT = 1;

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

    public MemberRole getHighestStatus() {
        return Stream.of(MemberRole.values())
                .filter(roles::contains)
                .reduce((f, s) -> s)
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

    public void add(MemberRole newRole) {
        checkRoleIsSuperAdmin(newRole);
        deleteIfNewRoleIsNonMemberAndRolesHasOnlyOneMemberRole(newRole);
        deleteAllIfNewRoleIsMemberAndRolesHasNonMemberRolesOnly(newRole);
        checkRolesCanCoexist(newRole);

        roles.add(newRole);
        roles.addAll(newRole.getRequiredRoles());
    }

    public void delete(MemberRole deleteRole) {
        checkRoleIsSuperAdmin(deleteRole);
        checkHasConflictIfRoleIsDeleted(deleteRole);

        roles.remove(deleteRole);

        if (roles.isEmpty()) {
            roles.add(MemberRole.PUBLIC);
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(MemberRole::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private void deleteIfNewRoleIsNonMemberAndRolesHasOnlyOneMemberRole(MemberRole newRole) {
        if (containsOnlyMemberRole()) {
            if (!newRole.isMember()) {
                roles.remove(MemberRole.MEMBER);
            }
        }
    }

    private void deleteAllIfNewRoleIsMemberAndRolesHasNonMemberRolesOnly(MemberRole newRole) {
        if (newRole.equals(MemberRole.MEMBER)) {
            if (roles.stream().noneMatch(MemberRole::isMember)) {
                roles.clear();
            }
        }
    }

    private boolean containsOnlyMemberRole() {
        return roles.size() == ONLY_ONE_ELEMENT && roles.contains(MemberRole.MEMBER);
    }

    private void checkRoleIsSuperAdmin(MemberRole role) {
        if (role.equals(MemberRole.SUPER_ADMIN)) {
            throw new UnauthorizedException("Super admin role cannot be added");
        }
    }

    private void checkHasConflictIfRoleIsDeleted(MemberRole deleteRole) {
        List<MemberRole> rolesWithoutDeleteRole = roles.stream()
                .filter(not(deleteRole::equals))
                .collect(Collectors.toList());

        List<String> rolesNeededToDelete = rolesWithoutDeleteRole.stream()
                .filter(role -> !rolesArePresentIn(role.getRequiredRoles(), rolesWithoutDeleteRole))
                .map(MemberRole::name)
                .collect(Collectors.toList());

        if (!rolesNeededToDelete.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Role %s could not be deleted. The following roles should be deleted first: %s",
                            deleteRole.name(), String.join(", ", rolesNeededToDelete)));
        }
    }

    private boolean rolesArePresentIn(List<MemberRole> requiredRoles, List<MemberRole> rolesWithoutDeleteRole) {
        return rolesWithoutDeleteRole.containsAll(requiredRoles);
    }

    private void checkRolesCanCoexist(MemberRole newRole) {
        roles.stream()
                .filter(role -> role.isMember() != newRole.isMember())
                .findFirst()
                .ifPresent(role -> {
                    throw new IllegalArgumentException(
                            String.format("Please delete role %s before adding new role %s",
                                    role.name(), newRole.name()));
                });
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
