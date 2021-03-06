package org.poolc.api.poolc.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdatePoolcRequest {
    private final String presidentName;
    private final String phoneNumber;
    private final String location;
    private final String location_url;
    private final String introduction;
    private final String mainImageUrl;
    private final Boolean isSubscriptionPeriod;
    private final String applyUri;

    @JsonCreator
    public UpdatePoolcRequest(String presidentName, String phoneNumber, String location, String location_url, String introduction, String mainImageUrl, Boolean isSubscriptionPeriod, String applyLinkUri) {
        this.presidentName = presidentName;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.location_url = location_url;
        this.introduction = introduction;
        this.mainImageUrl = mainImageUrl;
        this.isSubscriptionPeriod = isSubscriptionPeriod;
        this.applyUri = applyLinkUri;
    }
}
