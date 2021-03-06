package org.poolc.api.activity.vo;

import lombok.Getter;
import org.poolc.api.activity.dto.ActivityRequest;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ActivityCreateValues {

    private final String title;
    private final String description;
    private final LocalDate startDate;
    private final Boolean isSeminar;
    private final String classHour;
    private final Long capacity;
    private final List<String> tags;
    private final Long hour;
    private final List<String> fileList;

    public ActivityCreateValues(ActivityRequest request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.startDate = request.getStartDate();
        this.isSeminar = request.getSeminar();
        this.classHour = request.getClassHour();
        this.capacity = request.getCapacity();
        this.tags = request.getTags();
        this.hour = request.getHour();
        this.fileList = request.getFileList();
    }
}
