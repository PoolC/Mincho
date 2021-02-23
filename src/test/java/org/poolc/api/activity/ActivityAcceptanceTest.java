package org.poolc.api.activity;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.activity.dto.ActivityCreateRequest;
import org.poolc.api.auth.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles("activityTest")
public class ActivityAcceptanceTest extends AcceptanceTest {

    @Test
    public void 프로젝트전체조회() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getActivitiesRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("data")).hasSize(3);
    }

    @Test
    public void 프로젝트하나조회() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getActivityRequest(accessToken, 6l);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void 없는프로젝트조회시에러() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getActivityRequest(accessToken, 655l);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void 프로젝트등록() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        LocalDate localDate = LocalDate.now();
        List<String> tags = new ArrayList<>();
        tags.add("꿀잼");
        tags.add("깨꿀잼");
        ExtractableResponse<Response> response = createActivityRequest(accessToken, "김성하의 재미있는 sql세미나", "이거 들으면 취업가능", localDate, true, "3시간", 200l, tags);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response2 = getActivitiesRequest(accessToken);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.body().jsonPath().getList("data")).hasSize(4);
        
    }

    public static ExtractableResponse<Response> getActivitiesRequest(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/activity")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getActivityRequest(String accessToken, Long id) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/activity/{activityID}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> createActivityRequest(String token, String title, String description, LocalDate startDate, Boolean isSeminar, String classHour, Long capacity, List<String> tags) {
        ActivityCreateRequest request = new ActivityCreateRequest(title, description, startDate, isSeminar, classHour, capacity, tags);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/activity")
                .then().log().all()
                .extract();
    }
}
