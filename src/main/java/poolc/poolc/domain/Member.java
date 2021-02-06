package poolc.poolc.domain;

import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity(name = "Member")
@Getter
public class Member {

    @Id
    @Column(name = "UUID", length = 40)
    private String UUID;

    @Column(name = "loginID", columnDefinition = "varchar(40)", unique = true, nullable = false)
    private String loginID;

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

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "passwordResetToken", columnDefinition = "varchar(255)", nullable = false)
    private String passwordResetToken;

    @Column(name = "passwordResetTokenValidUntil", nullable = false)
    private LocalDateTime passwordResetTokenValidUntil;

    @Column(name = "profileImageURL", columnDefinition = "varchar(255)")
    private String profileImageURL;

    @Column(name = "introduction", columnDefinition = "varchar(1024)")
    private String introduction;

    @Column(name = "isExcepted", columnDefinition = "boolean default false")
    private Boolean isExcepted = false;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "passwordHash", nullable = false)
    private byte[] passwordHash;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "passwordSalt", nullable = false)
    private byte[] passwordSalt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    List<ProjectMember> projects = new ArrayList<>();

    public Member() {
    }

    public Member(String UUID, String loginID, String email, String phoneNumber, String name, String department, String studentID, Boolean isActivated, Boolean isAdmin, LocalDateTime createdAt, LocalDateTime updatedAt, String passwordResetToken, LocalDateTime passwordResetTokenValidUntil, String profileImageURL, String introduction, Boolean isExcepted, byte[] passwordHash, byte[] passwordSalt, List<ProjectMember> projects) {
        this.UUID = UUID;
        this.loginID = loginID;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.department = department;
        this.studentID = studentID;
        this.isActivated = isActivated;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetTokenValidUntil = passwordResetTokenValidUntil;
        this.profileImageURL = profileImageURL;
        this.introduction = introduction;
        this.isExcepted = isExcepted;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.projects = projects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(getUUID(), member.getUUID()) &&
                Objects.equals(getLoginID(), member.getLoginID()) &&
                Objects.equals(getEmail(), member.getEmail()) &&
                Objects.equals(getPhoneNumber(), member.getPhoneNumber()) &&
                Objects.equals(getName(), member.getName()) &&
                Objects.equals(getDepartment(), member.getDepartment()) &&
                Objects.equals(getStudentID(), member.getStudentID()) &&
                Objects.equals(getIsActivated(), member.getIsActivated()) &&
                Objects.equals(getIsAdmin(), member.getIsAdmin()) &&
                Objects.equals(getCreatedAt(), member.getCreatedAt()) &&
                Objects.equals(getUpdatedAt(), member.getUpdatedAt()) &&
                Objects.equals(getPasswordResetToken(), member.getPasswordResetToken()) &&
                Objects.equals(getPasswordResetTokenValidUntil(), member.getPasswordResetTokenValidUntil()) &&
                Objects.equals(getProfileImageURL(), member.getProfileImageURL()) &&
                Objects.equals(getIntroduction(), member.getIntroduction()) &&
                Objects.equals(getIsExcepted(), member.getIsExcepted()) &&
                Arrays.equals(getPasswordHash(), member.getPasswordHash()) &&
                Arrays.equals(getPasswordSalt(), member.getPasswordSalt());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getUUID(), getLoginID(), getEmail(), getPhoneNumber(), getName(), getDepartment(), getStudentID(), getIsActivated(), getIsAdmin(), getCreatedAt(), getUpdatedAt(), getPasswordResetToken(), getPasswordResetTokenValidUntil(), getProfileImageURL(), getIntroduction(), getIsExcepted(), getProjects());
        result = 31 * result + Arrays.hashCode(getPasswordHash());
        result = 31 * result + Arrays.hashCode(getPasswordSalt());
        return result;
    }
}