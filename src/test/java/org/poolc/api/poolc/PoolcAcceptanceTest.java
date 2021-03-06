package org.poolc.api.poolc;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.poolc.dto.CreatePoolcRequest;
import org.poolc.api.poolc.dto.UpdatePoolcRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

public class PoolcAcceptanceTest extends AcceptanceTest {
    @BeforeEach
    public void init() {
        String accessToken = 임원진로그인();
        CreatePoolcRequest request = CreatePoolcRequest.builder()
                .presidentName("정윤석")
                .phoneNumber("01026763034")
                .introduction("게임제작 동아리")
                .location("공학관A 537호")
                .mainImageUrl(null)
                .applyUri(null)
                .isSubscriptionPeriod(false)
                .locationUrl(null)
                .build();

        ExtractableResponse<Response> poolcRequest = createPoolcRequest(accessToken, request);
    }

    @Test
    void 회원풀씨정보조회() {
        String accessToken = 비임원진로그인();

        ExtractableResponse<Response> response = getPoolcRequest(accessToken);
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void public풀씨정보조회() {
        ExtractableResponse<Response> response = getPoolcRequestNoLogin();
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 임원진_풀씨정보업데이트() {
        String accessToken = 임원진로그인();
        UpdatePoolcRequest request = new UpdatePoolcRequest("전영주", "01067679584", "공A 537호", null, "프로그래밍 동아리", null, false, null);
        ExtractableResponse<Response> response = updatePoolcInfo(accessToken, request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 비임원진_풀씨정보업데이트() {
        String accessToken = 비임원진로그인();
        UpdatePoolcRequest request = new UpdatePoolcRequest("전영주", "01067679584", "공A 537호", null, "프로그래밍 동아리", null, false, null);
        ExtractableResponse<Response> response = updatePoolcInfo(accessToken, request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private String 임원진로그인() {
        return loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    private String 비임원진로그인() {
        return loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static ExtractableResponse<Response> createPoolcRequest(String accessToken, CreatePoolcRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/poolc")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getPoolcRequest(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/poolc")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getPoolcRequestNoLogin() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/poolc")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updatePoolcInfo(String accessToken, UpdatePoolcRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/poolc")
                .then().log().all()
                .extract();
    }
}
