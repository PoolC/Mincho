package org.poolc.api.activity.vo;

import lombok.Getter;
import org.poolc.api.activity.dto.AttendanceRequest;

import java.util.List;

@Getter
public class AttendanceValues {

    private final long sessionID;
    private final List<String> memberLoginIDs;

    public AttendanceValues(AttendanceRequest request) {
        this.sessionID = request.getSessionID();
        this.memberLoginIDs = request.getMemberLoginIDs();
    }
}
