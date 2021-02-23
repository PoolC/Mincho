package org.poolc.api.project.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

@Getter
public class ProjectMemberValues {

    private final String id;
    private final String name;

    @JsonCreator
    public ProjectMemberValues(Member member) {
        this.id = member.getUUID();
        this.name = member.getName();
    }
}