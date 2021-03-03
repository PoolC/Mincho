package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.activity.domain.ActivityTag;

@Getter
public class TagResponse {

    private final String name;

    @JsonCreator
    public TagResponse(ActivityTag tag) {
        this.name = tag.getContent();
    }
}
