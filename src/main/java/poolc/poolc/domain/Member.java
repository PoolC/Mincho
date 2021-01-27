package poolc.poolc.domain;

import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Member")
@Getter
public class Member {

    @Id
    @Column(name = "UUID", length = 40)
    private String UUID;

    @Column(name = "loginID", columnDefinition = "char(40)", unique = true, nullable = false)
    private String loginID;

    @Column(name = "email", columnDefinition = "char(255)", nullable = false, unique = true)
    private String email;

    @Column(name = "phoneNumber", columnDefinition = "char(20)", nullable = false)
    private String phoneNumber;

    @Column(name = "name", columnDefinition = "char(40)", nullable = false)
    private String name;

    @Column(name = "department", columnDefinition = "char(40)", nullable = false)
    private String department;

    @Column(name = "studentID", columnDefinition = "char(40)", nullable = false)
    private String studentID;

    @Column(name = "isActivated", columnDefinition = "boolean default false")
    private Boolean isActivated = false;

    @Column(name = "isAdmin", columnDefinition = "boolean default false")
    private Boolean isAdmin = false;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "passwordResetToken", columnDefinition = "char(255)", nullable = false)
    private String passwordResetToken;

    @Column(name = "passwordResetTokenValidUntil", nullable = false)
    private LocalDateTime passwordResetTokenValidUntil;

    @Column(name = "profileImageURL", columnDefinition = "char(255)")
    private String profileImageURL;

    @Column(name = "introduction", columnDefinition = "char(1024)")
    private String introduction;

    @Column(name = "isExcepted", columnDefinition = "boolean default false")
    private Boolean isExcepted = false;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "passwordHash", nullable = false)
    private Blob passwordHash;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "passwordSalt", nullable = false)
    private Blob passwordSalt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    List<ProjectMember> projects = new ArrayList<>();
}