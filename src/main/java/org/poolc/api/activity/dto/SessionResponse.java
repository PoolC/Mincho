package org.poolc.api.activity.dto;

import lombok.Getter;
import org.poolc.api.activity.domain.Session;

import java.time.LocalDate;

@Getter
public class SessionResponse {

    private final Long id;
    private final Long sessionNumber;
    private final LocalDate date;
    private final String description;

    public SessionResponse(Session session) {
        this.id = session.getId();
        this.sessionNumber = session.getSessionNumber();
        this.date = session.getDate();
        this.description = session.getDescription();
    }
}
