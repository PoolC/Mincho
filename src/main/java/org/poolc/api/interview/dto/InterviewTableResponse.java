package org.poolc.api.interview.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InterviewTableResponse {
    private final Long mySlotId;
    private final List<InterviewSlotsByDateResponse> data;

    @JsonCreator
    public InterviewTableResponse(Long mySlotId, List<InterviewSlotsByDateResponse> data) {
        this.mySlotId = mySlotId;
        this.data = data.stream().sorted(Comparator.comparing(InterviewSlotsByDateResponse::getDate)).collect(Collectors.toList());
    }
}
