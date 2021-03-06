package org.poolc.api.poolc.vo;

import lombok.Getter;
import org.poolc.api.poolc.dto.UpdatePoolcRequest;

@Getter
public class PoolcUpdateValues {
    private final String presidentName;
    private final String phoneNumber;
    private final String location;
    private final String locationUrl;
    private final String introduction;
    private final String mainImageUrl;
    private final Boolean isSubscriptionPeriod;
    private final String applyUri;

    public PoolcUpdateValues(UpdatePoolcRequest request) {
        this.presidentName = request.getPresidentName();
        this.phoneNumber = request.getPhoneNumber();
        this.location = request.getLocation();
        this.locationUrl = request.getLocationUrl();
        this.introduction = request.getIntroduction();
        this.mainImageUrl = request.getMainImageUrl();
        this.isSubscriptionPeriod = request.getIsSubscriptionPeriod();
        this.applyUri = request.getApplyUri();
    }
}
