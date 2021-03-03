package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.activity.domain.Session;

import java.time.LocalDate;

@Getter
public class SessionResponse {

    private final Long id;
    private final Long sessionNumber;
    private final LocalDate date;
    private final String description;

    @JsonCreator
    public SessionResponse(Long id, Long sessionNumber, LocalDate date, String description) {
        this.id = id;
        this.sessionNumber = sessionNumber;
        this.date = date;
        this.description = description;
    }

    public static SessionResponse of(Session session) {
        return new SessionResponse(session.getId(), session.getSessionNumber(), session.getDate(), session.getDescription());
    }
}
