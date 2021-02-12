package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final String email;
    private final String phoneNumber;
    private final String name;
    private final String department;
    private final String studentID;
    private final String profileImageURL;
    private final String introduction;

    @JsonCreator
    public MemberResponse(String email, String phoneNumber, String name, String department, String studentID, String profileImageURL, String introduction) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.department = department;
        this.studentID = studentID;
        this.profileImageURL = profileImageURL;
        this.introduction = introduction;
    }
}
