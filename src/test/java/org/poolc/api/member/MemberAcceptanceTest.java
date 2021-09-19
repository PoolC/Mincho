package org.poolc.api.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.*;

@ActiveProfiles("memberTest")
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class MemberAcceptanceTest extends AcceptanceTest {

    @Order(1)
    @Test
    void testCreate() {
        RegisterMemberRequest request = new RegisterMemberRequest("testName", "testId",
                "testPassword", "testPassword",
                "test@email.com", "010-1234-4321",
                "컴퓨터과학과", "2021147500", "자기소개", "https://api.poolc.org/files/%E1%84%83%E1%85%A9%E1%86%BC%E1%84%87%E1%85%A1%E1%86%BC%E1%84%8B%E1%85%A3%E1%86%A8%E1%84%83%E1%85%A9.png");
        ExtractableResponse<Response> response = createMemberRequest(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
    }

    @Order(2)
    @Test
    void testCreateWrongPasswordCheck() {
        RegisterMemberRequest request = new RegisterMemberRequest("WrongPassword", "WrongPasswordId",
                "WrongPassword", "testPassword1",
                "fffff@email.com", "010-1234-9999",
                "컴퓨터과학과", "2000147500", "자기소개", "https://api.poolc.org/files/%E1%84%83%E1%85%A9%E1%86%BC%E1%84%87%E1%85%A1%E1%86%BC%E1%84%8B%E1%85%A3%E1%86%A8%E1%84%83%E1%85%A9.png");
        ExtractableResponse<Response> response = createMemberRequest(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Order(3)
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

    @Order(4)
    @Test
    void testGetMe() {
        String accessToken = memberLogin();

        ExtractableResponse<Response> response = getMemberRequest(accessToken);
        MemberResponse responseBody = response.as(MemberResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getName()).isEqualTo("MEMBER_NAME");
    }

    @Order(5)
    @Test
    void getAllMembersAsAdmin() {
        String accessToken = adminLogin();

        ExtractableResponse<Response> response = getMembersRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MemberListResponse responseBody = response.body().as(MemberListResponse.class);
    }

    @Order(6)
    @Test
    void getAllMembersAsMember() {
        String accessToken = memberLogin();

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

    @Order(7)
    @Test
    void updateWrongPasswordCheckMemberInfo() {
        String accessToken = updateMemberLogin();
        ExtractableResponse<Response> response = updateMemberInfoRequest(accessToken, "NEW_MEMBER_NAME", "UPDATE_MEMBER_PASSWORD", "NEW_PASSWORD", "NEW@naver.com", "01033334444", "야이야");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Order(8)
    @Test
    void updateMemberInfo() {
        String accessToken = updateMemberLogin();

        ExtractableResponse<Response> response = updateMemberInfoRequest(accessToken, "NEW_MEMBER_NAME", "UPDATE_MEMBER_PASSWORD", "UPDATE_MEMBER_PASSWORD", "NEW@naver.com", "01033334444", "야이야");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> certifiedResponse = getMemberRequest(accessToken);
        MemberResponse responseBody = certifiedResponse.as(MemberResponse.class);
        assertThat(responseBody.getName()).isEqualTo("NEW_MEMBER_NAME");
    }

    @Order(9)
    @Test
    void ActivateMember() {
        String accessToken = adminLogin();

        String loginID = "UNACCEPTED_MEMBER_ID";

        ExtractableResponse<Response> response = ActivateMemberRequest(accessToken, loginID);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> certifiedResponse = AdminGetMemberRequestByLoginID(accessToken, loginID);
        MemberResponse responseBody = certifiedResponse.as(MemberResponse.class);
        assertThat(responseBody.getIsActivated()).isEqualTo(true);
    }

    @Order(10)
    @Test
    void promoteAsAdmin() {
        String accessToken = adminLogin();

        String not_admin_loginID = "NOT_ADMIN_ID";

        ExtractableResponse<Response> response = UpdateMemberIsAdmin(accessToken, not_admin_loginID);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> certifiedResponse = AdminGetMemberRequestByLoginID(accessToken, not_admin_loginID);
        MemberResponse responseBody = certifiedResponse.as(MemberResponse.class);
        assertThat(responseBody.getIsAdmin()).isEqualTo(true);
    }

    @Order(11)
    @Test
    void revokeAdminPrivileges() {
        String accessToken = adminLogin();

        String willRevokeAdminID = "WILL_REVOKE_ADMIN_ID";

        ExtractableResponse<Response> response = UpdateMemberIsAdmin(accessToken, willRevokeAdminID);
        ExtractableResponse<Response> certifiedResponse = AdminGetMemberRequestByLoginID(accessToken, willRevokeAdminID);
        MemberResponse responseBody = certifiedResponse.as(MemberResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getIsAdmin()).isEqualTo(false);
    }

    @Order(12)
    @Test
    public void 임원진X_UNACCEPTANCE회원_전체_삭제시_에러() {
        //given
        String accessToken = memberLogin();

        //when
        ExtractableResponse<Response> response = deleteUnacceptedDeleteRequest(accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Order(13)
    @Test
    public void 임원진_UNACCEPTANCE회원_전체_삭제() {
        //given
        String accessToken = adminLogin();

        //when
        ExtractableResponse<Response> response = deleteUnacceptedDeleteRequest(accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> checkMemberNumber = getMembersRequest(accessToken);

        MemberListResponse allMembers = checkMemberNumber.as(MemberListResponse.class);
        long unacceptedMembersCount = allMembers.getData().stream().filter(Predicate.not(MemberResponse::getIsActivated)).count();
        assertThat(unacceptedMembersCount).isEqualTo(0L);

    }

    @Order(14)
    @Test
    void adminUpdatesMemberStatusAsExpelled() {
        String accessToken = adminLogin();
        ExtractableResponse<Response> response = adminUpdateMemberStatusRequest(accessToken, "TO_BE_EXPELLED_ID", "EXPELLED");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @Order(15)
    @Test
    void adminUpdatesMemberStatus() {
        String accessToken = adminLogin();
        ExtractableResponse<Response> response = adminUpdateMemberStatusRequest(accessToken, "MEMBER_ID", MemberRole.GRADUATED.name());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        accessToken = memberLogin();
        response = getMemberRequest(accessToken);
        MemberResponse memberResponse = response.as(MemberResponse.class);

        assertThat(memberResponse.getRole()).isEqualTo(MemberRole.GRADUATED.name());
    }

    @Order(16)
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

    @Order(17)
    @Test
    void updateIsExcepted() {
        String accessToken = adminLogin();
        ExtractableResponse<Response> response = updateMemberIsExceptedRequest(accessToken, "MEMBER_ID");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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

    public static ExtractableResponse<Response> deleteUnacceptedDeleteRequest(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/member/unaccepted")
                .then().log().all()
                .extract();
    }
}
