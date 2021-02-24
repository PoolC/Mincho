package org.poolc.api.activity;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.activity.dto.ActivityCreateRequest;
import org.poolc.api.activity.dto.ActivityUpdateRequest;
import org.poolc.api.activity.dto.SessionCreateRequest;
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
    public void 액티비티전체조회() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getActivitiesRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("data")).hasSize(3);
    }

    @Test
    public void 액티비티하나조회() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getActivityRequest(accessToken, 6l);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void 없는액티비티조회시에러() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getActivityRequest(accessToken, 655l);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void 액티비티등록() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        LocalDate localDate = LocalDate.now();
        List<String> tags = new ArrayList<>();
        tags.add("꿀잼");
        tags.add("깨꿀잼");
        ExtractableResponse<Response> response = createActivityRequest(accessToken, "김성하의 재미있는 sql세미나", "이거 들으면 취업가능", localDate, true, "3시간", 200l, tags, 200l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response2 = getActivitiesRequest(accessToken);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.body().jsonPath().getList("data")).hasSize(4);

    }

    @Test
    public void 본인이액티비티수정() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        LocalDate localDate = LocalDate.now();
        List<String> tags = new ArrayList<>();
        tags.add("꿀잼");
        tags.add("깨꿀잼");
        tags.add("한시간만 들어도 알고리즘 정복가능");
        ExtractableResponse<Response> response = updateActivityRequest(accessToken, 1l, "김성하의 재미있는 sql세미나", "이거 들으면 취업가능", localDate, true, "3시간", 200l, tags, 200l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response2 = getActivitiesRequest(accessToken);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.body().jsonPath().getList("data")).hasSize(3);

    }

    @Test
    public void 없는액티비티수정() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        LocalDate localDate = LocalDate.now();
        List<String> tags = new ArrayList<>();
        tags.add("꿀잼");
        tags.add("깨꿀잼");
        tags.add("한시간만 들어도 알고리즘 정복가능");
        ExtractableResponse<Response> response = updateActivityRequest(accessToken, 432l, "김성하의 재미있는 sql세미나", "이거 들으면 취업가능", localDate, true, "3시간", 200l, tags, 200l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());


    }

    @Test
    public void 임원진이액티비티수정() {
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();

        LocalDate localDate = LocalDate.now();
        List<String> tags = new ArrayList<>();
        tags.add("꿀잼");
        tags.add("깨꿀잼");
        tags.add("한시간만 들어도 알고리즘 정복가능");
        ExtractableResponse<Response> response = updateActivityRequest(accessToken, 1l, "김성하의 재미있는 sql세미나", "이거 들으면 취업가능", localDate, true, "3시간", 200l, tags, 200l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response2 = getActivitiesRequest(accessToken);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.body().jsonPath().getList("data")).hasSize(3);

    }

    @Test
    public void 호스트나임원진이아닌사람이액티비티수정() {
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();

        LocalDate localDate = LocalDate.now();
        List<String> tags = new ArrayList<>();
        tags.add("꿀잼");
        tags.add("깨꿀잼");
        tags.add("한시간만 들어도 알고리즘 정복가능");

        ExtractableResponse<Response> response = updateActivityRequest(accessToken, 1l, "김성하의 재미있는 sql세미나", "이거 들으면 취업가능", localDate, true, "3시간", 200l, tags, 200l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    public void 호스트가액티비티삭제() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteActivityRequest(accessToken, 3l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response2 = getActivitiesRequest(accessToken);

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.body().jsonPath().getList("data")).hasSize(2);

    }

    @Test
    public void 없는액티비티삭제() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteActivityRequest(accessToken, 432l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());


    }

    @Test
    public void 임원진이액티비티삭제() {
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();


        ExtractableResponse<Response> response = deleteActivityRequest(accessToken, 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void 호스트나임원진이아닌사람이액티비티삭제() {
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();

        LocalDate localDate = LocalDate.now();
        List<String> tags = new ArrayList<>();
        tags.add("꿀잼");
        tags.add("깨꿀잼");
        tags.add("한시간만 들어도 알고리즘 정복가능");

        ExtractableResponse<Response> response = deleteActivityRequest(accessToken, 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    public void 호스트가회차정보입력() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = createSessionRequest(accessToken, 1l, 1l, "김성하의c++세미나 1회차", LocalDate.now());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void 없는액티비티정보입력() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = createSessionRequest(accessToken, 10l, 1l, "김성하의c++세미나 1회차", LocalDate.now());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void 호스트가아닌사람이액티비티정보입력() {
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = createSessionRequest(accessToken, 1l, 1l, "김성하의c++세미나 1회차", LocalDate.now());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    public void 세션정보조회() {
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getSessionsRequest(accessToken, 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

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

    public static ExtractableResponse<Response> createActivityRequest(String token, String title, String description, LocalDate startDate, Boolean isSeminar, String classHour, Long capacity, List<String> tags, Long hour) {
        ActivityCreateRequest request = new ActivityCreateRequest(title, description, startDate, isSeminar, classHour, capacity, hour, tags);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/activity")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateActivityRequest(String token, Long id, String title, String description, LocalDate startDate, Boolean isSeminar, String classHour, Long capacity, List<String> tags, Long hour) {
        ActivityUpdateRequest request = new ActivityUpdateRequest(title, description, startDate, isSeminar, classHour, capacity, hour, tags);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/activity/{activityID}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteActivityRequest(String token, Long id) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/activity/{activityID}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> createSessionRequest(String token, Long activityID, Long sessionID, String description, LocalDate date) {
        SessionCreateRequest request = new SessionCreateRequest(activityID, sessionID, date, description);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/activity/session")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getSessionsRequest(String token, Long id) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/activity/session/{activityID}", id)
                .then().log().all()
                .extract();
    }
}
