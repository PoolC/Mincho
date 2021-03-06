package org.poolc.api.poolc.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.poolc.domain.Poolc;

@Getter
public class PoolcResponse {
    private final String presidentName;
    private final String phoneNumber;
    private final String location;
    private final String locationUrl;
    private final String introduction;
    private final String mainImageUrl;
    private final Boolean isSubscriptionPeriod;
    private final String applyUri;

    @JsonCreator
    public PoolcResponse(String presidentName, String phoneNumber, String location, String location_url, String introduction, String mainImageUrl, Boolean isSubscriptionPeriod, String applyLingUri) {
        this.presidentName = presidentName;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.locationUrl = location_url;
        this.introduction = introduction;
        this.mainImageUrl = mainImageUrl;
        this.isSubscriptionPeriod = isSubscriptionPeriod;
        this.applyUri = applyLingUri;
    }

    public static PoolcResponse of(Poolc poolc) {
        return new PoolcResponse(poolc.getPresidentName(), poolc.getPhoneNumber(), poolc.getLocation(), poolc.getLocationUrl(), poolc.getIntroduction(), poolc.getMainImageUrl(), poolc.getIsSubscriptionPeriod(), poolc.getApplyUri());
    }

}
