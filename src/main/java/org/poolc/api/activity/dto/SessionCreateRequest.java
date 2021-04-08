package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SessionCreateRequest {

    private final Long activityID;
    private final Long sessionNumber;
    private final LocalDate date;
    private final String description;
    private final List<String> fileList;

    @JsonCreator
    public SessionCreateRequest(Long activityID, Long sessionNumber, LocalDate date, String description, List<String> fileList) {
        this.activityID = activityID;
        this.sessionNumber = sessionNumber;
        this.date = date;
        this.description = description;
        this.fileList = fileList;
    }
}
