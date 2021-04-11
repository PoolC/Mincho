package org.poolc.api.batch.vo.member;

import lombok.Getter;
import lombok.Setter;
import org.poolc.api.member.domain.MemberRole;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class MemberDao {

    private String uuid;

    private String login_id;

    private String password_hash;

    private String email;

    private String phone_number;

    private String name;

    private String department;

    private String student_id;

    private String password_reset_token;

    private Timestamp password_reset_token_valid_until;

    private String profile_image_url;

    private String introduction;

    private Boolean is_excepted;

    private Timestamp created_at;

    private Timestamp updated_at;

    private String roles;

    public MemberDao() {

    }

    public MemberDao(String uuid, String login_id, String password_hash, String email, String phone_number, String name, String department, String student_id, String password_reset_token, Timestamp password_reset_token_valid_until, String profile_image_url, String introduction, Boolean is_excepted, Timestamp created_at, Timestamp updated_at, String roles) {
        this.uuid = uuid;
        this.login_id = login_id;
        this.password_hash = password_hash;
        this.email = email;
        this.phone_number = phone_number;
        this.name = name;
        this.department = department;
        this.student_id = student_id;
        this.password_reset_token = password_reset_token;
        this.password_reset_token_valid_until = password_reset_token_valid_until;
        this.profile_image_url = profile_image_url;
        this.introduction = introduction;
        this.is_excepted = is_excepted;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.roles = roles;
    }

    public void of(Members value, String password) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.uuid = value.getUuid();
        this.login_id = value.getLoginId();
        this.password_hash = password;
        this.email = value.getEmail();
        this.phone_number = value.getPhoneNumber();
        this.name = value.getName();
        this.department = value.getDepartment();
        this.student_id = value.getStudentId();
        this.password_reset_token = value.getPasswordResetToken();
        this.password_reset_token_valid_until = null;
        this.profile_image_url = null;
        this.introduction = null;
        this.is_excepted = null;
        this.created_at = Timestamp.valueOf(LocalDateTime.parse(value.getCreatedAt().substring(0, 19), formatter));
        this.updated_at = Timestamp.valueOf(LocalDateTime.parse(value.getCreatedAt().substring(0, 19), formatter));
        this.roles = checkIsAdminOrIsAccepted(value);
    }

    public String checkIsAdminOrIsAccepted(Members value) {
        if (stringifyBoolean(value.getIsActivated())) {
            return MemberRole.MEMBER.name();
        }
        return MemberRole.PUBLIC.name();
    }

    public Boolean stringifyBoolean(String str) {
        if (str.equals("t"))
            return true;
        else
            return false;
    }
}
