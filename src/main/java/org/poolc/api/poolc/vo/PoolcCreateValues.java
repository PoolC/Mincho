package org.poolc.api.poolc.vo;

import lombok.Getter;
import org.poolc.api.poolc.dto.CreatePoolcRequest;

@Getter
public class PoolcCreateValues {
    private final String presidentName;
    private final String phoneNumber;
    private final String location;
    private final String location_url;
    private final String introduction;
    private final String mainImageUrl;
    private final Boolean isSubscriptionPeriod;
    private final String applyUri;

    public PoolcCreateValues(CreatePoolcRequest request) {
        this.presidentName = request.getPresidentName();
        this.phoneNumber = request.getPhoneNumber();
        this.location = request.getLocation();
        this.location_url = request.getLocation_url();
        this.introduction = request.getIntroduction();
        this.mainImageUrl = request.getMainImageUrl();
        this.isSubscriptionPeriod = request.getIsSubscriptionPeriod();
        this.applyUri = request.getApplyUri();
    }
}
