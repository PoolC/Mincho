package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SessionUpdateRequest {
    private final LocalDate date;
    private final String description;
    private final List<String> fileList;

    @JsonCreator
    public SessionUpdateRequest(LocalDate date, String description, List<String> fileList) {
        this.date = date;
        this.description = description;
        this.fileList = fileList;
    }
}
