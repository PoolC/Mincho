package org.poolc.api.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
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
import java.util.stream.Collectors;

@Entity(name = "Member")
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends TimestampEntity implements UserDetails {
    // TODO: 시그니처 통일해주세요(unique, nullable 순서)
    // TODO: db 컬럼명은 snake_case 가 컨벤션입니다. 이왕 name field를 선언한거 snake_case에 맞게 바꿔주세요

    @Id
    @Column(name = "UUID", length = 40)
    private String UUID;

    @Column(name = "loginID", columnDefinition = "varchar(40)", unique = true, nullable = false)
    private String loginID;

    @Column(name = "passwordHash", nullable = false)
    private String passwordHash;

    @Column(name = "email", columnDefinition = "varchar(255)", nullable = false, unique = true)
    private String email;

    @Column(name = "phoneNumber", columnDefinition = "varchar(20)", nullable = false)
    private String phoneNumber;

    @Column(name = "name", columnDefinition = "varchar(40)", nullable = false)
    private String name;

    @Column(name = "department", columnDefinition = "varchar(40)", nullable = false)
    private String department;

    @Column(name = "studentID", columnDefinition = "varchar(40)", nullable = false, unique = true)
    private String studentID;

    @Column(name = "passwordResetToken", columnDefinition = "varchar(255)")
    private String passwordResetToken;

    @Column(name = "passwordResetTokenValidUntil")
    private LocalDateTime passwordResetTokenValidUntil;

    @Column(name = "profileImageURL", columnDefinition = "varchar(255)")
    private String profileImageURL;

    @Column(name = "introduction", columnDefinition = "varchar(1024)")
    private String introduction;

    @Column(name = "isExcepted", columnDefinition = "boolean default false")
    private Boolean isExcepted;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "member_uuid"))
    @Builder.Default
    private Set<MemberRoles> roles = new HashSet<>();

    protected Member() {
    }

    public Member(String UUID, String loginID, String passwordHash, String email, String phoneNumber, String name, String department, String studentID, String passwordResetToken, LocalDateTime passwordResetTokenValidUntil, String profileImageURL, String introduction, Boolean isExcepted, Set<MemberRoles> roles) {
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
    }

    public boolean isAcceptedMember() {
        return !roles.contains(MemberRoles.UNACCEPTED);
    }

    public boolean isAdmin() {
        return roles.contains(MemberRoles.ADMIN);
    }

    public void acceptMember() {
        roles.remove(MemberRoles.UNACCEPTED);
        roles.add(MemberRoles.MEMBER);
    }

    public void changeAdminPrivileges(boolean toAdmin) {
        if (toAdmin) {
            grantAdminPrivileges();
            return;
        }

        revokeAdminPrivileges();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(MemberRoles::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
        return !roles.contains(MemberRoles.UNACCEPTED);
    }

    @Override
    public boolean isAccountNonExpired() {
        return !roles.contains(MemberRoles.EXPELLED);
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
        roles.add(MemberRoles.ADMIN);
    }

    private void revokeAdminPrivileges() {
        roles.remove(MemberRoles.ADMIN);
    }
}