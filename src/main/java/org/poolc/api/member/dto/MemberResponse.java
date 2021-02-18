package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

@Getter
public class MemberResponse {
    private final String loginID;
    private final String email;
    private final String phoneNumber;
    private final String name;
    private final String department;
    private final String studentID;
    private final String profileImageURL;
    private final String introduction;


    @JsonCreator
    public MemberResponse(Member member) {
        this.loginID = member.getLoginID();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();
        this.name = member.getName();
        this.department = member.getDepartment();
        this.studentID = member.getStudentID();
        this.profileImageURL = member.getProfileImageURL();
        this.introduction = member.getIntroduction();
    }
}
