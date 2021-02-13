package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class RegisterMemberRequest {
    private final String name;
    private final String loginID;
    private final String password;
    private final String passwordCheck;
    private final String email;
    private final String phoneNumber;
    private final String department;
    private final String studentID;

    @JsonCreator
    public RegisterMemberRequest(String name, String loginID, String password, String passwordCheck, String email, String phoneNumber, String department, String studentID) {
        this.name = name;
        this.loginID = loginID;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.studentID = studentID;
    }
}
