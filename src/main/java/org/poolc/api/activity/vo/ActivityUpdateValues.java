package org.poolc.api.activity.vo;

import lombok.Getter;
import org.poolc.api.activity.dto.ActivityRequest;

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
    private final Long hour;

    public ActivityUpdateValues(ActivityRequest request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.startDate = request.getStartDate();
        this.isSeminar = request.getIsSeminar();
        this.classHour = request.getClassHour();
        this.capacity = request.getCapacity();
        this.tags = request.getTags();
        this.hour = request.getHour();
    }
}
