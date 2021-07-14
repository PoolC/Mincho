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

    @Test
    void tokenIsIssued() {
        ExtractableResponse<Response> response = loginRequest("MEMBER_ID", "MEMBER_PASSWORD");

        AuthResponse authResponse = response.as(AuthResponse.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(authResponse.getAccessToken()).isNotNull();
    }

    @Test
    void loginWrongPassword() {
        ExtractableResponse<Response> response = loginRequest("MEMBER_ID", "MEMBER_PASSWOR");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void unauthorizedUser() {
        String loginID = "non_existing_loginID";
        String password = "non_existing_password";

        ExtractableResponse<Response> response = loginRequest(loginID, password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void unactivatedUser() {
        String accessToken = adminLogin();

        UpdatePoolcRequest request = new UpdatePoolcRequest("전영주", "01067679584", "공A 537호", null, "프로그래밍 동아리", null, false, null);
        PoolcAcceptanceTest.updatePoolcInfo(accessToken, request);

        String loginID = "UNACCEPTED_MEMBER_ID";
        String password = "UNACCEPTED_MEMBER_PASSWORD";

        ExtractableResponse<Response> response = loginRequest(loginID, password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 가입신청기간일시UNACCEPTED회원로그인() {
        String accessToken = adminLogin();

        UpdatePoolcRequest request = new UpdatePoolcRequest("전영주", "01067679584", "공A 537호", null, "프로그래밍 동아리", null, true, null);
        PoolcAcceptanceTest.updatePoolcInfo(accessToken, request);

        String loginID = "UNACCEPTED_MEMBER_ID";
        String password = "UNACCEPTED_MEMBER_PASSWORD";

        ExtractableResponse<Response> response = loginRequest(loginID, password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    public static String adminLogin() {
        return loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static String memberLogin() {
        return loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static String unacceptanceLogin() {
        return loginRequest("UNACCEPTED_MEMBER_ID", "UNACCEPTED_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static String unacceptance1Login() {
        return loginRequest("UNACCEPTED_MEMBER_ID1", "UNACCEPTED_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static String unacceptance2Login() {
        return loginRequest("UNACCEPTED_MEMBER_ID2", "UNACCEPTED_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static String unacceptance3Login() {
        return loginRequest("UNACCEPTED_MEMBER_ID3", "UNACCEPTED_MEMBER_PASSWORD")
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
