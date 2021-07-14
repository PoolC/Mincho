package org.poolc.api.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles("memberTest")
public class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void testCreate() {
        RegisterMemberRequest request = new RegisterMemberRequest("testName", "testId",
                "testPassword", "testPassword",
                "test@email.com", "010-1234-4321",
                "컴퓨터과학과", "2021147500", "자기소개", "https://api.poolc.org/files/%E1%84%83%E1%85%A9%E1%86%BC%E1%84%87%E1%85%A1%E1%86%BC%E1%84%8B%E1%85%A3%E1%86%A8%E1%84%83%E1%85%A9.png");
        ExtractableResponse<Response> response = createMemberRequest(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
    }

    @Test
    void testCreateWrongPasswordCheck() {
        RegisterMemberRequest request = new RegisterMemberRequest("WrongPassword", "WrongPasswordId",
                "WrongPassword", "testPassword1",
                "fffff@email.com", "010-1234-9999",
                "컴퓨터과학과", "2000147500", "자기소개", "https://api.poolc.org/files/%E1%84%83%E1%85%A9%E1%86%BC%E1%84%87%E1%85%A1%E1%86%BC%E1%84%8B%E1%85%A3%E1%86%A8%E1%84%83%E1%85%A9.png");
        ExtractableResponse<Response> response = createMemberRequest(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testDuplicateCreate() {
        RegisterMemberRequest request = new RegisterMemberRequest("DuplicateName", "DuplicateTestId",
                "DuplicateTestPassword", "DuplicateTestPassword",
                "fffff@email.com", "010-1234-9999",
                "컴퓨터과학과", "2000146500", "자기소개", "https://api.poolc.org/files/%E1%84%83%E1%85%A9%E1%86%BC%E1%84%87%E1%85%A1%E1%86%BC%E1%84%8B%E1%85%A3%E1%86%A8%E1%84%83%E1%85%A9.png");
        createMemberRequest(request);

        ExtractableResponse<Response> notPass = createMemberRequest(request);

        assertThat(notPass.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
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
        assertThat(responseBody.getData()).hasSize(10);
    }

    @Test
    void getAllMembersAsMember() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getMembersRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MemberListResponse responseBody = response.body().as(MemberListResponse.class);
        Long memberStatuses = responseBody.getData().stream()
                .map(MemberResponse::getRole)
                .map(MemberRole::valueOf)
                .map(MemberRole::isHideInfo)
                .filter(value -> value.equals(true))
                .count();

        assertThat(memberStatuses).isEqualTo(0);
    }

    @Test
    void updateWrongPasswordCheckMemberInfo() {
        String accessToken = loginRequest("UPDATE_MEMBER_ID", "UPDATE_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateMemberInfoRequest(accessToken, "NEW_MEMBER_NAME", "UPDATE_MEMBER_PASSWORD", "NEW_PASSWORD", "NEW@naver.com", "01033334444", "야이야");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updateMemberInfo() {
        String accessToken = loginRequest("UPDATE_MEMBER_ID", "UPDATE_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateMemberInfoRequest(accessToken, "NEW_MEMBER_NAME", "UPDATE_MEMBER_PASSWORD", "UPDATE_MEMBER_PASSWORD", "NEW@naver.com", "01033334444", "야이야");

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
    void promoteAsAdmin() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        String not_admin_loginID = "NOT_ADMIN_ID";

        ExtractableResponse<Response> response = UpdateMemberIsAdmin(accessToken, not_admin_loginID);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> certifiedResponse = AdminGetMemberRequestByLoginID(accessToken, not_admin_loginID);
        MemberResponse responseBody = certifiedResponse.as(MemberResponse.class);
        assertThat(responseBody.getIsAdmin()).isEqualTo(true);
    }

    @Test
    void revokeAdminPrivileges() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        String willRevokeAdminID = "WILL_REVOKE_ADMIN_ID";

        ExtractableResponse<Response> response = UpdateMemberIsAdmin(accessToken, willRevokeAdminID);
        ExtractableResponse<Response> certifiedResponse = AdminGetMemberRequestByLoginID(accessToken, willRevokeAdminID);
        MemberResponse responseBody = certifiedResponse.as(MemberResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getIsAdmin()).isEqualTo(false);
    }

    @Test
    void deleteMemberAsNonAdminIsUnauthorized() {
        String accessToken = loginRequest("UPDATE_MEMBER_ID", "UPDATE_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteMemberRequest(accessToken, "WILL_DELETE_MEMBER_ID");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void adminUpdatesMemberStatusAsExpelled() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = adminUpdateMemberStatusRequest(accessToken, "TO_BE_EXPELLED_ID", "EXPELLED");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void adminUpdatesMemberStatus() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = adminUpdateMemberStatusRequest(accessToken, "MEMBER_ID", MemberRole.GRADUATED.name());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        response = getMemberRequest(accessToken);
        MemberResponse memberResponse = response.as(MemberResponse.class);

        assertThat(memberResponse.getRole()).isEqualTo(MemberRole.GRADUATED.name());
    }

    @Test
    void selfUpdateStatus() {
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = updateMemberStatusRequest(accessToken, new ToggleRoleRequest(MemberRole.COMPLETE.name()));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        response = getMemberRequest(accessToken);
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponse.getRole()).isEqualTo(MemberRole.COMPLETE.name());
    }

    @Test
    void updateIsExcepted() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = updateMemberIsExceptedRequest(accessToken, "MEMBER_ID");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("테스트 01. 임원진이 UNACCEPTANCE 자격인 모든 회원 정상적으로 삭제")
    public void 임원진_UNACCEPTANCE회원_전체_삭제() {
        //given

        //when

        //then

    }

    @Test
    @DisplayName("테스트 02. 임원진이 UNACCEPTANCE 자격인 모든 회원 삭제시 에러 발생 ")
    public void 임원진X_UNACCEPTANCE회원_전체_삭제시_에러() {
        //given

        //when

        //then

    }

    public static ExtractableResponse<Response> createMemberRequest(RegisterMemberRequest request) {
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

    public static ExtractableResponse<Response> updateMemberInfoRequest(String accessToken, String name, String password, String passwordCheck, String email, String phoneNumber, String introduction) {
        UpdateMemberRequest request = new UpdateMemberRequest(name, password, passwordCheck, email, phoneNumber, introduction, "https://api.poolc.org/files/%E1%84%83%E1%85%A9%E1%86%BC%E1%84%87%E1%85%A1%E1%86%BC%E1%84%8B%E1%85%A3%E1%86%A8%E1%84%83%E1%85%A9.png");

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

    public static ExtractableResponse<Response> UpdateMemberIsAdmin(String accessToken, String loginID) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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

    public static ExtractableResponse<Response> adminUpdateMemberStatusRequest(String accessToken, String targetMemberLoginID, String role) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(Collections.singletonMap("role", role))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/member/role/{loginID}", targetMemberLoginID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateMemberStatusRequest(String accessToken, ToggleRoleRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/member/role")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateMemberIsExceptedRequest(String accessToken, String loginId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/member/excepted/{loginId}", loginId)
                .then().log().all()
                .extract();
    }
}
