package org.poolc.api.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.dto.UpdateMemberRequest;
import org.poolc.api.project.domain.ProjectMember;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "Member")
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends TimestampEntity {
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

    @Column(name = "isActivated", columnDefinition = "boolean default false")
    private Boolean isActivated = false;

    @Column(name = "isAdmin", columnDefinition = "boolean default false")
    private Boolean isAdmin = false;

    @Column(name = "passwordResetToken", columnDefinition = "varchar(255)")
    private String passwordResetToken;

    @Column(name = "passwordResetTokenValidUntil")
    private LocalDateTime passwordResetTokenValidUntil;

    @Column(name = "profileImageURL", columnDefinition = "varchar(255)")
    private String profileImageURL;

    @Column(name = "introduction", columnDefinition = "varchar(1024)")
    private String introduction;

    @Column(name = "isExcepted", columnDefinition = "boolean default false")
    private Boolean isExcepted = false;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    List<ProjectMember> projects;

    protected Member() {
    }

    public Member(String UUID, String loginID, String passwordHash, String email, String phoneNumber, String name, String department, String studentID, Boolean isActivated, Boolean isAdmin, String passwordResetToken, LocalDateTime passwordResetTokenValidUntil, String profileImageURL, String introduction, Boolean isExcepted, List<ProjectMember> projects) {
        this.UUID = UUID;
        this.loginID = loginID;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.department = department;
        this.studentID = studentID;
        this.isActivated = isActivated;
        this.isAdmin = isAdmin;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetTokenValidUntil = passwordResetTokenValidUntil;
        this.profileImageURL = profileImageURL;
        this.introduction = introduction;
        this.isExcepted = isExcepted;
        this.projects = projects;
    }

    public void update(UpdateMemberRequest updateMemberRequest, String passwordHash) {
        this.name = updateMemberRequest.getName();
        this.passwordHash = passwordHash;
        this.email = updateMemberRequest.getEmail();
        this.phoneNumber = updateMemberRequest.getPhoneNumber();
    }
}