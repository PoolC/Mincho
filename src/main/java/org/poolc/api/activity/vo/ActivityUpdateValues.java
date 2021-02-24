package org.poolc.api.activity.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.activity.dto.ActivityUpdateRequest;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ActivityUpdateValues {

    private final String title;
    private final String description;
    private final LocalDate startDate;
    private final Boolean isSeminar;
    private final String classHour;
    private final Long capacity;
    private final List<String> tags;
    private final String memberID;
    private final Long hour;

    @JsonCreator
    public ActivityUpdateValues(ActivityUpdateRequest request, String id) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.startDate = request.getStartDate();
        this.isSeminar = request.getIsSeminar();
        this.classHour = request.getClassHour();
        this.capacity = request.getCapacity();
        this.tags = request.getTags();
        this.memberID = id;
        this.hour = request.getHour();
    }
}
