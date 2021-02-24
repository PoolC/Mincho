package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class AttendanceRequest {

    private final long sessionID;
    private final List<Long> members;

    @JsonCreator
    public AttendanceRequest(long sessionID, List<Long> members) {
        this.sessionID = sessionID;
        this.members = members;
    }
}
