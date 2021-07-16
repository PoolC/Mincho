package org.poolc.api.interview.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import org.poolc.api.interview.domain.InterviewSlot;
import org.poolc.api.member.dto.MemberResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InterviewSlotResponse {
    private final Long slotId;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int capacity;
    private final List<MemberResponse> interviewees;

    @Builder
    @JsonCreator
    public InterviewSlotResponse(Long slotId, LocalDate date, LocalTime startTime, LocalTime endTime, int capacity, List<MemberResponse> interviewees) {
        this.slotId = slotId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.interviewees = interviewees;
    }

    public InterviewSlotResponse(InterviewSlot slot, boolean isAdmin) {
        this.slotId = slot.getId();
        this.date = slot.getDate();
        this.startTime = slot.getStartTime();
        this.endTime = slot.getEndTime();
        this.capacity = slot.getCapacity();
        this.interviewees = slot.getInterviewees()
                .stream()
                .map(member -> new MemberResponse(member, isAdmin))
                .collect(Collectors.toList());
    }
}
