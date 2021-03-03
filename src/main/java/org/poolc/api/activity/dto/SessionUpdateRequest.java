package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SessionUpdateRequest {
    private final LocalDate date;
    private final String description;

    @JsonCreator
    public SessionUpdateRequest(LocalDate date, String description) {
        this.date = date;
        this.description = description;
    }
}
