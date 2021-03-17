package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.activity.domain.Activity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ActivityResponse {
    private final Long id;
    private final String title;
    private final HostResponse host;
    private final String classHour;
    private final LocalDate startDate;
    private final boolean available;
    private final List<TagResponse> tags;
    private final Long capacity;
    private final boolean isSeminar;
    private final Long hour;
    private final String description;
    private final List<String> memberLoginIds;

    @JsonCreator
    public ActivityResponse(Long id, String title, HostResponse host, String classHour, LocalDate startDate, boolean available, List<TagResponse> tags, Long capacity, boolean isSeminar, Long hour, String description, List<String> memberLoginIds) {
        this.id = id;
        this.title = title;
        this.host = host;
        this.classHour = classHour;
        this.startDate = startDate;
        this.available = available;
        this.tags = tags;
        this.capacity = capacity;
        this.isSeminar = isSeminar;
        this.hour = hour;
        this.description = description;
        this.memberLoginIds = memberLoginIds;
    }


//    @JsonCreator
//    public ActivityResponse(Long id, String title, HostResponse host, String classHour, LocalDate startDate, boolean available, List<TagResponse> tags, Long capacity, boolean isSeminar, Long hour, String description) {
//        this.id = id;
//        this.title = title;
//        this.host = host;
//        this.classHour = classHour;
//        this.startDate = startDate;
//        this.available = available;
//        this.tags = tags;
//        this.capacity = capacity;
//        this.isSeminar = isSeminar;
//        this.hour = hour;
//        this.description = description;
//    }


    public static ActivityResponse of(Activity activity) {
        List<TagResponse> tags = activity.getTags().stream()
                .map(t -> new TagResponse(t))
                .collect(Collectors.toList());
        HostResponse host = HostResponse.of(activity.getHost());
        return new ActivityResponse(activity.getId(), activity.getTitle(), host,
                activity.getClassHour(), activity.getStartDate(), activity.getAvailable(), tags, activity.getCapacity(), activity.getIsSeminar(), activity.getHour(), activity.getDescription(), activity.getMemberLoginIDs());
    }
}
