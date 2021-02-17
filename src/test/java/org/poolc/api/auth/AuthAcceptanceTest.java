package org.poolc.api.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthRequest;
import org.poolc.api.auth.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class AuthAcceptanceTest extends AcceptanceTest {
    @Test
    void tokenIsIssued() {
        ExtractableResponse<Response> response = loginRequest("MEMBER_ID", "MEMBER_PASSWORD");

        AuthResponse authResponse = response.as(AuthResponse.class);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(authResponse.getAccessToken()).isNotNull();
    }

    @Test
    void unauthorizedUser() {
        String loginID = "non_existing_loginID";
        String password = "non_existing_password";

        ExtractableResponse<Response> response = loginRequest(loginID, password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void unactivatedUser() {
        String loginID = "UNACCEPTED_MEMBER_ID";
        String password = "UNACCEPTED_MEMBER_PASSWORD";

        ExtractableResponse<Response> response = loginRequest(loginID, password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
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
