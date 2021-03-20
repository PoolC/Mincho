package org.poolc.api.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.dto.UpdateMemberRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity(name = "Member")
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends TimestampEntity implements UserDetails {
    // TODO: 시그니처 통일해주세요(unique, nullable 순서)

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

    @Embedded
    private MemberRoles roles;

    protected Member() {
    }

    public Member(String UUID, String loginID, String passwordHash, String email, String phoneNumber, String name, String department, String studentID, String passwordResetToken, LocalDateTime passwordResetTokenValidUntil, String profileImageURL, String introduction, Boolean isExcepted, MemberRoles roles) {
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
        return roles.isMember();
    }

    public boolean isAdmin() {
        return roles.isAdmin();
    }

    public String getStatus() {
        return roles.getHighestStatus().name();
    }

    public boolean shouldHide() {
        return roles.getHighestStatus().isHideInfo();
    }

    public void acceptMember() {
        roles = MemberRoles.getDefaultFor(MemberRole.MEMBER);
    }

    public void quit() {
        roles = MemberRoles.getDefaultFor(MemberRole.QUIT);
    }

    public void adminAddsRoleFor(Member targetMember, MemberRole role) {
        onlyAdmin();

        targetMember.getRoles().add(role);
    }

    public void adminDeletesRoleFor(Member targetMember, MemberRole role) {
        onlyAdmin();

        targetMember.getRoles().delete(role);
    }

    public void toggleRole(Member targetMember, MemberRole role) {
        onlyAdmin();

        if (targetMember.getRoles().hasRole(role)) {
            adminDeletesRoleFor(targetMember, role);
            return;
        }

        adminAddsRoleFor(targetMember, role);
    }

    public void selfToggleRole(MemberRole role) {
        if (roles.hasRole(role)) {
            selfDeleteRole(role);
            return;
        }

        selfAddRole(role);
    }

    public void selfAddRole(MemberRole role) {
        checkHasCorrectPermissions(role);

        roles.add(role);
    }

    public void selfDeleteRole(MemberRole role) {
        checkHasCorrectPermissions(role);

        roles.delete(role);
    }

    public void toggleExcept(Member targetMember) {
        onlyAdmin();

        targetMember.toggleIsExcepted();
    }

    private void onlyAdmin() {
        if (!isAdmin()) {
            throw new UnauthorizedException("Only admins can do this");
        }
    }

    private void checkHasCorrectPermissions(MemberRole role) {
        if (!role.isSelfToggleable()) {
            throw new UnauthorizedException(String.format("Role %s cannot be self toggled", role.name()));
        }

        if (role.isOnlyAdminToggleable() && !isAdmin()) {
            throw new UnauthorizedException(String.format("Role %s can only be toggled by admin", role.name()));
        }
    }

    private void toggleIsExcepted() {
        isExcepted = !isExcepted;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.getAuthorities();
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
        return roles.isAcceptedMember();
    }

    @Override
    public boolean isAccountNonExpired() {
        return roles.isExpelled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
