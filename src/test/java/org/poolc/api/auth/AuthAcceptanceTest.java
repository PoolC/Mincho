package org.poolc.api.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthRequest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.poolc.PoolcAcceptanceTest;
import org.poolc.api.poolc.dto.UpdatePoolcRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("memberTest")
public class AuthAcceptanceTest extends AcceptanceTest {
    public static String admin_id = "ADMIN_ID", admin_password = "ADMIN_PASSWORD",
            member_id = "MEMBER_ID", member_password = "MEMBER_PASSWORD",
            unaccepted_member_id = "UNACCEPTED_MEMBER_ID", unaccepted_member_password = "UNACCEPTED_MEMBER_PASSWORD",
            unaccepted_member_id1 = "UNACCEPTED_MEMBER_ID1", unaccepted_member_id2 = "UNACCEPTED_MEMBER_ID2",
            unaccepted_member_id3 = "UNACCEPTED_MEMBER_ID3", wrong_password = "WRONG_PASSWORD",
            not_existing_loginId = "NON_EXISTING_LOGIN_ID";

    @Test
    void tokenIsIssued() {
        ExtractableResponse<Response> response = loginRequest(member_id, member_password);

        AuthResponse authResponse = response.as(AuthResponse.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(authResponse.getAccessToken()).isNotNull();
    }

    @Test
    void loginWrongPassword() {
        ExtractableResponse<Response> response = loginRequest(member_id, wrong_password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void unauthorizedUser() {
        ExtractableResponse<Response> response = loginRequest(not_existing_loginId, wrong_password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void unactivatedUser() {
        String accessToken = adminLogin();

        UpdatePoolcRequest request = new UpdatePoolcRequest("전영주", "01067679584", "공A 537호", null, "프로그래밍 동아리", null, false, null);
        PoolcAcceptanceTest.updatePoolcInfo(accessToken, request);

        ExtractableResponse<Response> response = loginRequest(unaccepted_member_id, unaccepted_member_password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 가입신청기간일시UNACCEPTED회원로그인() {
        String accessToken = adminLogin();

        UpdatePoolcRequest request = new UpdatePoolcRequest("전영주", "01067679584", "공A 537호", null, "프로그래밍 동아리", null, true, null);
        PoolcAcceptanceTest.updatePoolcInfo(accessToken, request);
        
        ExtractableResponse<Response> response = loginRequest(unaccepted_member_id, unaccepted_member_password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static String adminLogin() {
        return loginRequest(admin_id, admin_password)
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static String memberLogin() {
        return loginRequest(member_id, member_password)
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static String unacceptanceLogin() {
        return loginRequest(unaccepted_member_id, unaccepted_member_password)
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static String unacceptance1Login() {
        return loginRequest(unaccepted_member_id1, unaccepted_member_password)
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static String unacceptance2Login() {
        return loginRequest(unaccepted_member_id2, unaccepted_member_password)
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static String unacceptance3Login() {
        return loginRequest(unaccepted_member_id3, unaccepted_member_password)
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static ExtractableResponse<Response> loginRequest(String loginID, String password) {
        AuthRequest request = new AuthRequest(loginID, password);

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .extract();
    }
}
