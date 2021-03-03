package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class AttendanceRequest {

    private final long sessionID;
    private final List<String> memberLoginIDs;

    @JsonCreator
    public AttendanceRequest(long sessionID, List<String> members) {
        this.sessionID = sessionID;
        this.memberLoginIDs = members;
    }
}
