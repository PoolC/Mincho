package org.poolc.api.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.member.dto.MemberListResponse;
import org.poolc.api.member.dto.MemberResponse;
import org.poolc.api.member.dto.RegisterMemberRequest;
import org.poolc.api.member.dto.UpdateMemberRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles("test")
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
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getMemberRequest(accessToken);
        MemberResponse responseBody = response.as(MemberResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getName()).isEqualTo("MEMBER_NAME");
    }

    @Test
    void getAllMembersAsAdmin() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getMembersRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MemberListResponse responseBody = response.body().as(MemberListResponse.class);
        assertThat(responseBody.getData()).hasSize(6);
    }

    @Test
    void getAllMembersAsNonAdminIsUnauthorized() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getMembersRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void updateWrongPasswordCheckMemberInfo() {
        String accessToken = loginRequest("UPDATE_MEMBER_ID", "UPDATE_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateMemberInfoRequest(accessToken, "NEW_MEMBER_NAME", "UPDATE_MEMBER_PASSWORD", "NEW_PASSWORD", "NEW@naver.com", "01033334444");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    void updateMemberInfo() {
        String accessToken = loginRequest("UPDATE_MEMBER_ID", "UPDATE_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateMemberInfoRequest(accessToken, "NEW_MEMBER_NAME", "UPDATE_MEMBER_PASSWORD", "UPDATE_MEMBER_PASSWORD", "NEW@naver.com", "01033334444");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> certifiedResponse = getMemberRequest(accessToken);
        MemberResponse responseBody = certifiedResponse.as(MemberResponse.class);
        assertThat(responseBody.getName()).isEqualTo("NEW_MEMBER_NAME");
    }

    @Test
    void ActivateMember() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        String loginID = "UNACCEPTED_MEMBER_ID";

        ExtractableResponse<Response> response = ActivateMemberRequest(accessToken, loginID);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> certifiedResponse = AdminGetMemberRequestByLoginID(accessToken, loginID);
        MemberResponse responseBody = certifiedResponse.as(MemberResponse.class);
        assertThat(responseBody.getIsActivated()).isEqualTo(true);
    }

    @Test
    void updateMemberIsAdmin() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        String not_admin_loginID = "NOT_ADMIN_ID";

        ExtractableResponse<Response> response = UpdateMemberIsAdmin(accessToken, not_admin_loginID, true);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> certifiedResponse = AdminGetMemberRequestByLoginID(accessToken, not_admin_loginID);
        MemberResponse responseBody = certifiedResponse.as(MemberResponse.class);
        assertThat(responseBody.getIsAdmin()).isEqualTo(true);
    }

    @Test
    void adminDeleteExistingMember() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteMemberRequest(accessToken, "WILL_DELETE_MEMBER_ID");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void adminDeleteNotExistingMember() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteMemberRequest(accessToken, "NO_MEMBER_ID");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deleteMemberAsNonAdminIsUnauthorized() {
        String accessToken = loginRequest("UPDATE_MEMBER_ID", "UPDATE_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteMemberRequest(accessToken, "WILL_DELETE_MEMBER_ID");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
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

    public static ExtractableResponse<Response> AdminGetMemberRequestByLoginID(String accessToken, String loginID) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/member/{loginID}", loginID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateMemberInfoRequest(String accessToken, String name, String password, String passwordCheck, String email, String phoneNumber) {
        UpdateMemberRequest request = new UpdateMemberRequest(name, password, passwordCheck, email, phoneNumber);

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

    public static ExtractableResponse<Response> ActivateMemberRequest(String accessToken, String loginID) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/member/activate/{loginID}", loginID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> UpdateMemberIsAdmin(String accessToken, String loginID, Boolean isAdmin) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(isAdmin)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/member/admin/{loginID}", loginID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteMemberRequest(String accessToken, String loginID) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/member/{loginID}", loginID)
                .then().log().all()
                .extract();
    }
}
