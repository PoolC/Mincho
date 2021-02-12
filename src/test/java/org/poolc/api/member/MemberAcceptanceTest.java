package org.poolc.api.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.member.dto.MemberResponse;
import org.poolc.api.member.dto.RegisterMemberRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void testCreate() {
        ExtractableResponse<Response> response = createMemberRequest("testName", "testId",
                "testPassword", "testPassword",
                "test@email.com", "010-1234-4321",
                "컴퓨터과학과", "2021147500");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
    }

    @Test
    void testGetMe() {
        // TODO: loginID와 password 계속 중복되고 있는데, CMD + R 로 한번에 바꿔보아요
        // Dataloader에 인증된 회원이 있다고 가정
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getMemberRequest(accessToken);
        MemberResponse responseBody = response.as(MemberResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getName()).isEqualTo("MEMBER_NAME");
    }

    @Test
    void getMeAsUnacceptedMemberIsUnauthorized() {
        // Dataloader에 비허가 회원이 있다고 가정
        String accessToken = loginRequest("UNACCEPTED_MEMBER_ID", "UNACCEPTED_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getMemberRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void getAllMembersAsAdmin() {
        // 임원진이 요청 보낸다고 가정
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getMembersRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().as(List.class)).hasSize(5); // TODO: 만든 회원 수에 맞게 변경
    }

    @Test
    void getAllMembersAsNonAdminIsUnauthorized() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getMembersRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void updateMemberInfo() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateMemberInfoRequest(accessToken, "NEW_MEMBER_NAME", "NEW_PASSWORD", "NEW_PASSWORD", "풀씨학과");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void adminCanDeleteMember() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteMemberRequest(accessToken, "TODO: 여기에 뭐가 들어가야될까요..");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deleteMemberAsNonAdminIsUnauthorized() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteMemberRequest(accessToken, "TODO: 여기에 뭐가 들어가야될까요..");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> createMemberRequest(String name, String loginID, String password, String passwordCheck, String email, String phone, String department, String studentID) {
        RegisterMemberRequest request = new RegisterMemberRequest(name, loginID, password, passwordCheck, email, phone, department, studentID);

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/member")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getMemberRequest(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/member/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getMembersRequest(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/member")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateMemberInfoRequest(String accessToken, String name, String password, String passwordCheck, String department) {
        // TODO: RegisterMemberRequest인데 update value를 넘기고 있네요. 어떻게 못 바꿀까요?
        RegisterMemberRequest request = new RegisterMemberRequest(name, null, password, passwordCheck, null, null, department, null);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/member/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteMemberRequest(String accessToken, String name) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/member", name)
                .then().log().all()
                .extract();
    }
}
