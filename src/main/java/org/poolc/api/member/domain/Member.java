package org.poolc.api.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.dto.UpdateMemberRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Entity(name = "Member")
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends TimestampEntity implements UserDetails {
    // TODO: 시그니처 통일해주세요(unique, nullable 순서)
    // TODO: db 컬럼명은 snake_case 가 컨벤션입니다. 이왕 name field를 선언한거 snake_case에 맞게 바꿔주세요

    @Id
    @Column(name = "uuid", length = 40)
    private String UUID;

    @Column(name = "login_id", columnDefinition = "varchar(40)", unique = true, nullable = false)
    private String loginID;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "email", columnDefinition = "varchar(255)", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", columnDefinition = "varchar(20)", nullable = false)
    private String phoneNumber;

    @Column(name = "name", columnDefinition = "varchar(40)", nullable = false)
    private String name;

    @Column(name = "department", columnDefinition = "varchar(40)", nullable = false)
    private String department;

    @Column(name = "student_id", columnDefinition = "varchar(40)", nullable = false, unique = true)
    private String studentID;

    @Column(name = "password_reset_token", columnDefinition = "varchar(255)")
    private String passwordResetToken;

    @Column(name = "password_reset_token_valid_until")
    private LocalDateTime passwordResetTokenValidUntil;

    @Column(name = "profile_image_url", columnDefinition = "varchar(255)")
    private String profileImageURL;

    @Column(name = "introduction", columnDefinition = "varchar(1024)")
    private String introduction;

    @Column(name = "is_excepted", columnDefinition = "boolean default false")
    private Boolean isExcepted;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "member_uuid"))
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new HashSet<>();

    protected Member() {
    }

    public Member(String UUID, String loginID, String passwordHash, String email, String phoneNumber, String name, String department, String studentID, String passwordResetToken, LocalDateTime passwordResetTokenValidUntil, String profileImageURL, String introduction, Boolean isExcepted, Set<MemberRole> roles) {
        this.UUID = UUID;
        this.loginID = loginID;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.department = department;
        this.studentID = studentID;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetTokenValidUntil = passwordResetTokenValidUntil;
        this.profileImageURL = profileImageURL;
        this.introduction = introduction;
        this.isExcepted = isExcepted;
        this.roles = roles;

        checkRolesAreCorrect();
    }

    public void updateMemberInfo(UpdateMemberRequest updateMemberRequest, String passwordHash) {
        this.name = updateMemberRequest.getName();
        this.passwordHash = passwordHash;
        this.email = updateMemberRequest.getEmail();
        this.phoneNumber = updateMemberRequest.getPhoneNumber();
        this.introduction = updateMemberRequest.getIntroduction();
        this.profileImageURL = updateMemberRequest.getProfileImageURL();
    }

    public boolean isAcceptedMember() {
        return isEnabled();
    }

    public boolean isMember() {
        return roles.contains(MemberRole.MEMBER);
    }

    public boolean isAdmin() {
        return roles.stream()
                .anyMatch(MemberRole::isAdmin);
    }

    public String getStatus() {
        return getHighestStatusRole().name();
    }

    public boolean shouldHide() {
        return getHighestStatusRole().isHideInfo();
    }

    public void adminUpdatesStatusOf(Member targetMember, MemberRole newRole) {
        onlyAdmin();

        targetMember.updateStatus(newRole);
    }

    public void acceptMember() {
        updateStatus(MemberRole.MEMBER);
    }

    public void updateStatus(MemberRole newRole) {
        roles = MemberRole.getRolesOf(newRole);
    }

    public void toggleAdmin(Member targetMember) {
        onlyAdmin();

        if (!targetMember.isMember()) {
            throw new IllegalStateException(String.format("Member %s is not a member yet", targetMember.getName()));
        }

        if (targetMember.isAdmin()) {
            targetMember.revokeAdminPrivileges();
            return;
        }

        targetMember.grantAdminPrivileges();
    }

    public void except(Member targetMember) {
        onlyAdmin();

        targetMember.toggleIsExcepted();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(MemberRole::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return loginID;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isEnabled() {
        return !roles.contains(MemberRole.UNACCEPTED);
    }

    @Override
    public boolean isAccountNonExpired() {
        return !roles.contains(MemberRole.EXPELLED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    private void grantAdminPrivileges() {
        roles.add(MemberRole.ADMIN);
    }

    private void revokeAdminPrivileges() {
        roles = roles.stream()
                .filter(Predicate.not(MemberRole.ADMIN::equals))
                .collect(Collectors.toSet());
    }

    private MemberRole getHighestStatusRole() {
        return MemberRole.getHighestStatus(roles);
    }

    private void onlyAdmin() {
        if (!this.isAdmin()) {
            throw new UnauthorizedException("Only admins can do this");
        }
    }

    private void toggleIsExcepted() {
        isExcepted = !isExcepted;
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
                .filter(Predicate.not(MemberRole::isMember))
                .findAny()
                .ifPresent(role -> {
                    throw new IllegalArgumentException("Member cannot have non-member role: " + role.name());
                });
    }

    private void checkIsSpecialRoleButDoesNotHaveMemberRole() {
        if (roles.contains(MemberRole.MEMBER)) {
            return;
        }

        roles.stream()
                .filter(MemberRole::isMember)
                .filter(Predicate.not(MemberRole.MEMBER::equals))
                .findAny()
                .ifPresent(specialRole -> {
                    throw new IllegalArgumentException(
                            String.format("Member %s has special role %s, but role %s was not present",
                                    name, specialRole.name(), MemberRole.MEMBER));
                });
    }
}
