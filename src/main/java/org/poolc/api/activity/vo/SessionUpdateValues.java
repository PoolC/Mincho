package org.poolc.api.activity.vo;

import lombok.Getter;
import org.poolc.api.activity.dto.SessionUpdateRequest;

import java.time.LocalDate;

@Getter
public class SessionUpdateValues {
    private final String uuid;
    private final LocalDate date;
    private final String description;

    public SessionUpdateValues(SessionUpdateRequest request, String uuid) {
        this.date = request.getDate();
        this.description = request.getDescription();
        this.uuid = uuid;
    }
}
