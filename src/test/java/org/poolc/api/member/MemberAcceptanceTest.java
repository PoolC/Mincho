package org.poolc.api.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.member.dto.RegisterMemberRequest;
import org.springframework.http.MediaType;

public class MemberAcceptanceTest extends AcceptanceTest {
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
}
