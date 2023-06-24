package org.poolc.api.tool;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles("memberTest")
public class ToolAcceptanceTest extends AcceptanceTest {

    @Test
    void Qr생성() {
        String testString = "https://docs.google.com/forms/d/e/1FAIpQLSfvL9hfDUDK2LdpOc9uythXcs2Os8vTcE-y42nE1LZKCTu1yg/viewform";
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = createQrRequest(accessToken, testString);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.IMAGE_PNG.toString());
    }

    @Test
    void 비회원은_QR_생성못함() {
        String testString = "test";
        String accessToken = "incorrect_token";

        ExtractableResponse<Response> response = createQrRequest(accessToken, testString);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> createQrRequest(String accessToken, String str) {
        return RestAssured
                .given().log().all()
                .queryParam("str", str)
                .auth().oauth2(accessToken)
                .contentType(MediaType.IMAGE_PNG_VALUE)
                .when().get("/tool/qr")
                .then().log().all()
                .extract();
    }
}
