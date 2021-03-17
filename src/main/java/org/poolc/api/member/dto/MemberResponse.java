package org.poolc.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.activity.dto.ActivityResponse;
import org.poolc.api.member.domain.Member;
import org.poolc.api.project.dto.ProjectResponse;

import java.util.List;

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
    private final List<ActivityResponse> hostActivities;
    private final List<ActivityResponse> participantActivities;
    private final List<ProjectResponse> projects;
    private final String status;

    @JsonCreator
    public MemberResponse(String loginID, String email, String phoneNumber, String name, String department, String studentID, String profileImageURL, String introduction, Boolean isActivated, Boolean isAdmin, List<ActivityResponse> hostActivities, List<ActivityResponse> participantActivities, List<ProjectResponse> projects, String status) {
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
        this.hostActivities = hostActivities;
        this.participantActivities = participantActivities;
        this.projects = projects;
        this.status = status;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getLoginID(), member.getEmail(), member.getPhoneNumber(), member.getName(), member.getDepartment(), member.getStudentID(), member.getProfileImageURL(), member.getIntroduction(), member.isAcceptedMember(), member.isAdmin(), null, null, null, member.getStatus());
    }

    public static MemberResponse of(Member member,
                                    List<ActivityResponse> hostActivities,
                                    List<ActivityResponse> participantActivities,
                                    List<ProjectResponse> projects) {
        return new MemberResponse(member.getLoginID(), member.getEmail(), member.getPhoneNumber(), member.getName(), member.getDepartment(), member.getStudentID(), member.getProfileImageURL(), member.getIntroduction(), member.isAcceptedMember(), member.isAdmin(), hostActivities, participantActivities, projects, member.getStatus());
    }
}
