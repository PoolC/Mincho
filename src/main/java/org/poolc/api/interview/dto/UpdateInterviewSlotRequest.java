package org.poolc.api.interview.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class UpdateInterviewSlotRequest {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int capacity;

    @Builder
    public UpdateInterviewSlotRequest(LocalTime startTime, LocalTime endTime, int capacity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
    }
}
