package org.poolc.api.interview.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InterviewSlotsByDateResponse {
    private final LocalDate date;
    private final List<InterviewSlotResponse> slots;

    @JsonCreator
    public InterviewSlotsByDateResponse(LocalDate date, List<InterviewSlotResponse> slots) {
        this.date = date;
        this.slots = slots.stream()
                .sorted(Comparator.comparing(InterviewSlotResponse::getStartTime))
                .collect(Collectors.toList());
    }
}
