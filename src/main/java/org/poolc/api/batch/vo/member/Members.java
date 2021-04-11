package org.poolc.api.batch.vo.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Members {
    private String uuid;
    private String loginId;
    private String passwordHash;
    private String passwordSalt;
    private String email;
    private String phoneNumber;
    private String name;
    private String department;
    private String studentId;
    private String isActivated;
    private String isAdmin;
    private String createdAt;
    private String updatedAt;
    private String passwordResetToken;
    private String passwordResetTokenValidUntil;

    public Members() {
    }

    public Members(String uuid, String loginId, String passwordHash, String passwordSalt, String email, String phoneNumber, String name, String department, String studentId, String isActivated, String isAdmin, String createdAt, String updatedAt, String passwordResetToken, String passwordResetTokenValidUntil) {
        this.uuid = uuid;
        this.loginId = loginId;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.department = department;
        this.studentId = studentId;
        this.isActivated = isActivated;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetTokenValidUntil = passwordResetTokenValidUntil;
    }
}
