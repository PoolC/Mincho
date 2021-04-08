package org.poolc.api.activity.vo;

import lombok.Getter;
import org.poolc.api.activity.dto.SessionCreateRequest;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SessionCreateValues {

    private final Long activityID;
    private final Long sessionNumber;
    private final LocalDate date;
    private final String description;
    private final Long hour;
    private final List<String> fileList;

    public SessionCreateValues(SessionCreateRequest request) {
        this.activityID = request.getActivityID();
        this.sessionNumber = request.getSessionNumber();
        this.date = request.getDate();
        this.description = request.getDescription();
        this.hour = request.getHour();
        this.fileList = request.getFileList();
    }
}
