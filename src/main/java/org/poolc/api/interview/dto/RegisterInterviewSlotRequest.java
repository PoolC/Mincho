package org.poolc.api.interview.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class RegisterInterviewSlotRequest {
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int capacity;

    @Builder
    public RegisterInterviewSlotRequest(LocalDate date, LocalTime startTime, LocalTime endTime, int capacity) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
    }
}
