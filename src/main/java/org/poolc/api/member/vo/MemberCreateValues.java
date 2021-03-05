package org.poolc.api.member.vo;

import lombok.Getter;
import org.poolc.api.member.dto.RegisterMemberRequest;

@Getter
public class MemberCreateValues {
    private final String name;
    private final String loginID;
    private final String password;
    private final String email;
    private final String phoneNumber;
    private final String department;
    private final String studentID;
    private final String introduction;

    public MemberCreateValues(RegisterMemberRequest request) {
        name = request.getName();
        loginID = request.getLoginID();
        password = request.getPassword();
        email = request.getEmail();
        phoneNumber = request.getPhoneNumber();
        department = request.getDepartment();
        studentID = request.getStudentID();
        introduction = request.getIntroduction();
    }
}
