package org.poolc.api.poolc.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;

//TODO: poolc 정보를 DB에 넣을 수 있으면 삭제
@Getter
@Builder
public class CreatePoolcRequest {
    private final String presidentName;
    private final String phoneNumber;
    private final String location;
    private final String locationUrl;
    private final String introduction;
    private final String mainImageUrl;
    private final Boolean isSubscriptionPeriod;
    private final String applyUri;

    @JsonCreator
    public CreatePoolcRequest(String presidentName, String phoneNumber, String location, String location_url, String introduction, String mainImageUrl, Boolean isSubscriptionPeriod, String applyUri) {
        this.presidentName = presidentName;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.locationUrl = location_url;
        this.introduction = introduction;
        this.mainImageUrl = mainImageUrl;
        this.isSubscriptionPeriod = isSubscriptionPeriod;
        this.applyUri = applyUri;
    }
}
