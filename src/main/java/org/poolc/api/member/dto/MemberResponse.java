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
    private final Boolean isActivated;
    private final Boolean isAdmin;

    @JsonCreator
    public MemberResponse(String loginID, String email, String phoneNumber, String name, String department, String studentID, String profileImageURL, String introduction, Boolean isActivated, Boolean isAdmin) {
        this.loginID = loginID;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.department = department;
        this.studentID = studentID;
        this.profileImageURL = profileImageURL;
        this.introduction = introduction;
        this.isActivated = isActivated;
        this.isAdmin = isAdmin;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getLoginID(), member.getEmail(), member.getPhoneNumber(), member.getName(), member.getDepartment(), member.getStudentID(), member.getProfileImageURL(), member.getIntroduction(), member.isAcceptedMember(), member.isAdmin());
    }
}
