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

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.member.MemberAcceptanceTest.createMemberRequest;

public class AuthAcceptanceTest extends AcceptanceTest {
    @Test
    void tokenIsIssued() {
        String loginID = "test";
        String password = "test";

        createMemberRequest("someName", loginID, password, password, "some@email.com", "010-2862-6046", "풀씨학부", "2021147500");
        ExtractableResponse<Response> response = loginRequest(loginID, password);

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
