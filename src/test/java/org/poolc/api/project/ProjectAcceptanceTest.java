package org.poolc.api.project;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.project.dto.RegisterProjectRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles("projectTest")
public class ProjectAcceptanceTest extends AcceptanceTest {

    @Test
    public void findProjects() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getProjectsRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("data")).hasSize(2);
    }

    @Test
    public void findProjectsWithUnAuthorizedMember() {
        ExtractableResponse<Response> response = getProjectsRequest("1234");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void findOneProject() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getProjectRequest(accessToken, 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("data.members").size()).isEqualTo(2);
    }

    @Test
    void findOneProjectDoesNotExist() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getProjectRequest(accessToken, 3l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void findMembersByName() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getMembersByNameRequest(accessToken, "MEMBER_NAME");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void findMembersByNameWithNonAdminUser() {
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getMembersByNameRequest(accessToken, "MEMBER_NAME");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void addProject() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        List<String> ids = new ArrayList<>();

        ExtractableResponse<Response> response = getMembersByNameRequest(accessToken, "MEMBER_NAME");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ids.add(response.body().jsonPath().getJsonObject("data[0].id").toString());
        ids.add(response.body().jsonPath().getJsonObject("data[1].id").toString());
        ExtractableResponse<Response> response2 = addProjectRequest(accessToken, "두번쨰 프로젝트", "배고파", "게임", "기간", "http://naver.com", "장난장난", ids);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void addProjectMemberDoesNotExist() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        List<String> ids = new ArrayList<>();
        ids.add("121323");
        ExtractableResponse<Response> response2 = addProjectRequest(accessToken, "두번쨰 프로젝트", "배고파", "게임", "기간", "http://naver.com", "장난장난", ids);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void addProjectWithNonAdminUser() {
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();
        List<String> ids = new ArrayList<>();
        ExtractableResponse<Response> response2 = addProjectRequest(accessToken, "두번쨰 프로젝트", "배고파", "게임", "기간", "http://naver.com", "장난장난", ids);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void updateProject() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        List<String> ids = new ArrayList<>();

        ExtractableResponse<Response> response = getMembersByNameRequest(accessToken, "MEMBER_NAME");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ids.add(response.body().jsonPath().getJsonObject("data[0].id").toString());
        ExtractableResponse<Response> response2 = updateProjectRequest(1l, accessToken, "두번쨰 프로젝트", "배고파", "게임", "기간", "http://naver.com", "장난장난", ids);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response3 = getProjectRequest(accessToken, 1l);
        assertThat(response3.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response3.body().jsonPath().getList("data.members").size()).isEqualTo(1l);
    }

    @Test
    void updateProjectWithNonExistingMember() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        List<String> ids = new ArrayList<>();

        ExtractableResponse<Response> response = getMembersByNameRequest(accessToken, "MEMBER_NAME");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ids.add("1234");
        ExtractableResponse<Response> response2 = updateProjectRequest(1l, accessToken, "두번쨰 프로젝트", "배고파", "게임", "기간", "http://naver.com", "장난장난", ids);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updateProjectDoesNotExist() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        List<String> ids = new ArrayList<>();

        ExtractableResponse<Response> response = getMembersByNameRequest(accessToken, "MEMBER_NAME");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ids.add(response.body().jsonPath().getJsonObject("data[0].id").toString());
        ExtractableResponse<Response> response2 = updateProjectRequest(45l, accessToken, "두번쨰 프로젝트", "배고파", "게임", "기간", "http://naver.com", "장난장난", ids);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updateProjectWithNonAdminUser() {
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();
        List<String> ids = new ArrayList<>();
        ExtractableResponse<Response> response2 = updateProjectRequest(1l, accessToken, "두번쨰 프로젝트", "배고파", "게임", "기간", "http://naver.com", "장난장난", ids);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void deleteProject() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response2 = deleteProjectRequest(1l, accessToken);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void deleteProjectdoesNotExist() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response2 = deleteProjectRequest(455l, accessToken);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deleteProjectWithNonAdminUser() {
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response2 = deleteProjectRequest(1l, accessToken);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> getProjectsRequest(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/project")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getProjectRequest(String accessToken, Long projectID) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/project/{projectID}", projectID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getMembersByNameRequest(String accessToken, String name) {
        return RestAssured
                .given().log().all()
                .queryParam("name", name)
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/project/member")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> addProjectRequest(String accessToken, String name, String description, String genre, String duration, String thumbnailURL, String body, List<String> ids) {
        RegisterProjectRequest request = new RegisterProjectRequest(name, description, genre, duration, thumbnailURL, body, ids);
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/project")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateProjectRequest(Long id, String accessToken, String name, String description, String genre, String duration, String thumbnailURL, String body, List<String> ids) {
        RegisterProjectRequest request = new RegisterProjectRequest(name, description, genre, duration, thumbnailURL, body, ids);
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/project/{projectID}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteProjectRequest(Long id, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/project/{projectID}", id)
                .then().log().all()
                .extract();
    }

}
