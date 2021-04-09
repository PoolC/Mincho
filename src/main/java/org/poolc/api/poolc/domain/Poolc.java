package org.poolc.api.poolc.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.poolc.vo.PoolcCreateValues;
import org.poolc.api.poolc.vo.PoolcUpdateValues;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "poolc")
@Getter
@Builder
public class Poolc {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "president_name", nullable = false, columnDefinition = "varchar(40)")
    private String presidentName;

    @Column(name = "phone_number", nullable = false, columnDefinition = "varchar(40)")
    private String phoneNumber;

    @Column(name = "location", nullable = false, columnDefinition = "varchar(100)")
    private String location;

    @Column(name = "location_url", columnDefinition = "varchar(1024)")
    private String locationUrl;

    @Column(name = "introduction", nullable = false, columnDefinition = "text")
    private String introduction;

    @Column(name = "main_image_url", columnDefinition = "varchar(1024)")
    private String mainImageUrl;

    @Column(columnDefinition = "boolean default false")
    private Boolean isSubscriptionPeriod;

    @Column(name = "apply_uri", columnDefinition = "varchar(1024)")
    private String applyUri;

    public Poolc() {
    }

    public Poolc(Long id, String presidentName, String phoneNumber, String location, String location_url, String introduction, String mainImageUrl, Boolean isSubscriptionPeriod, String applyUri) {
        this.id = id;
        this.presidentName = presidentName;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.locationUrl = location_url;
        this.introduction = introduction;
        this.mainImageUrl = mainImageUrl;
        this.isSubscriptionPeriod = isSubscriptionPeriod;
        this.applyUri = applyUri;
    }

    public Poolc(String presidentName, String phoneNumber, String location, String location_url, String introduction, String mainImageUrl, Boolean isSubscriptionPeriod, String applyUri) {
        this.presidentName = presidentName;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.locationUrl = location_url;
        this.introduction = introduction;
        this.mainImageUrl = mainImageUrl;
        this.isSubscriptionPeriod = isSubscriptionPeriod;
        this.applyUri = applyUri;
    }

    public void update(PoolcUpdateValues updateValues) {
        this.presidentName = updateValues.getPresidentName();
        this.phoneNumber = updateValues.getPhoneNumber();
        this.location = updateValues.getLocation();
        this.locationUrl = updateValues.getLocationUrl();
        this.introduction = updateValues.getIntroduction();
        this.mainImageUrl = updateValues.getMainImageUrl();
        this.isSubscriptionPeriod = updateValues.getIsSubscriptionPeriod();
        this.applyUri = updateValues.getApplyUri();
    }

    // TODO: DB에 poolc 정보를 넣을 수 있으면 삭제
    public static Poolc of(PoolcCreateValues createValues) {
        return new Poolc(createValues.getPresidentName(), createValues.getPhoneNumber(), createValues.getLocation(), createValues.getLocationUrl(), createValues.getIntroduction(), createValues.getMainImageUrl(), createValues.getIsSubscriptionPeriod(), createValues.getApplyUri());
    }
}
