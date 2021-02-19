package org.poolc.api.project.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectMemberResponse {

    private final String id;
    private final String name;
    private final String department;
    private final String studentID;

    @JsonCreator
    public ProjectMemberResponse(Member member) {
        this.id = member.getUUID();
        this.name = member.getName();
        this.department = member.getDepartment();
        this.studentID = member.getStudentID();
    }
}
