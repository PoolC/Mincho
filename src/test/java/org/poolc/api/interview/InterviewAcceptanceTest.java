package org.poolc.api.interview;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.AuthAcceptanceTest;
import org.poolc.api.interview.dto.InterviewSlotsByDateResponse;
import org.poolc.api.interview.dto.RegisterInterviewSlotRequest;
import org.poolc.api.interview.dto.UpdateInterviewSlotRequest;
import org.poolc.api.member.dto.MemberResponse;
import org.poolc.api.poolc.domain.Poolc;
import org.poolc.api.poolc.dto.UpdatePoolcRequest;
import org.poolc.api.poolc.repository.PoolcRespository;
import org.poolc.api.poolc.service.PoolcService;
import org.poolc.api.poolc.vo.PoolcUpdateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.*;

@ActiveProfiles(profiles = {"interviewTest", "memberTest"})
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class InterviewAcceptanceTest extends AcceptanceTest {
    @Autowired
    private PoolcService poolcService;

    @Autowired
    private PoolcRespository poolcRespository;

    @BeforeEach
    public void init() {
        UpdatePoolcRequest request = new UpdatePoolcRequest("전영주", "01067679584",
                "공A 537호", null, "프로그래밍 동아리", null,
                true, null);
        Poolc poolc = poolcService.get();
        poolc.update(new PoolcUpdateValues(request));
        poolcRespository.saveAndFlush(poolc);
    }

    @Test
    @DisplayName("테스트 01. 임원진이 아닌 회원이 interview table 조회시 login id 확인할 수 없지만 정상적으로 출력 200")
    public void 임원진X_Interview_table_조회() {
        //given
        Long applySlotId = 1L;
        String accessToken = unacceptanceLogin();
        applyInterview(accessToken, applySlotId);
        //when
        ExtractableResponse<Response> response = getInterviewSlots(accessToken);

        //then
        cancelApplyInterview(accessToken, applySlotId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MemberResponse memberDataCheck = response.body().jsonPath()
                .getList("data", InterviewSlotsByDateResponse.class).get(0)
                .getSlots().get(0)
                .getInterviewees().get(0);
        String unaccepted_member_id = "UNACCEPTED_MEMBER_ID";

        assertThat(response.body().jsonPath().getLong("mySlotId")).isEqualTo(applySlotId);
        assertThat(memberDataCheck.getLoginID()).isEqualTo(unaccepted_member_id);
        assertThat(memberDataCheck.getName()).isEqualTo(null);
        assertThat(memberDataCheck.getStudentID()).isEqualTo(null);
        assertThat(memberDataCheck.getDepartment()).isEqualTo(null);
        assertThat(memberDataCheck.getPhoneNumber()).isEqualTo(null);


    }

    @Test
    @DisplayName("테스트 02. 임원진이 전체 조회시 login id 확인할 수 있고 정상적으로 출력 200 ")
    public void 임원진_Interview_table_조회() {
        //given
        String accessToken = adminLogin();
        String applyAccessToken = unacceptanceLogin();
        long slotId = 1L;
        applyInterview(applyAccessToken, slotId);

        //when
        ExtractableResponse<Response> response = getInterviewSlots(accessToken);

        //then
        cancelApplyInterview(applyAccessToken, slotId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MemberResponse memberDataCheck = response.body().jsonPath()
                .getList("data", InterviewSlotsByDateResponse.class).get(0)
                .getSlots().get(0)
                .getInterviewees().get(0);
        String unaccepted_member_id = "UNACCEPTED_MEMBER_ID", member_name = "MEMBER_NAME2",
                member_department = "exampleDepartment", member_studentId = "2021147594",
                member_phoneNumber = "010-5555-5555";

        assertThat(response.body().jsonPath().getObject("mySlotId", Long.class)).isNull();
        assertThat(memberDataCheck.getLoginID()).isEqualTo(unaccepted_member_id);
        assertThat(memberDataCheck.getName()).isEqualTo(member_name);
        assertThat(memberDataCheck.getStudentID()).isEqualTo(member_studentId);
        assertThat(memberDataCheck.getDepartment()).isEqualTo(member_department);
        assertThat(memberDataCheck.getPhoneNumber()).isEqualTo(member_phoneNumber);
    }

    @Test
    @DisplayName("테스트 03. UNACCEPTANCE 회원이 정상적으로 면접시간 신청 200")
    public void UNACCEPTANCE회원_INTERVIEW_면접시간_신청() {
        //given
        String accessToken = unacceptanceLogin();
        long applySlotId = 1;

        //when
        ExtractableResponse<Response> response = applyInterview(accessToken, applySlotId);


        //then
        cancelApplyInterview(accessToken, applySlotId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getLong("mySlotId")).isEqualTo(applySlotId);
        MemberResponse memberDataCheck = response.body().jsonPath()
                .getList("data", InterviewSlotsByDateResponse.class).get(0)
                .getSlots().get(0)
                .getInterviewees().get(0);
        String unaccepted_member_id = "UNACCEPTED_MEMBER_ID", member_name = "MEMBER_NAME2",
                member_department = "exampleDepartment", member_studentId = "2021147594",
                member_phoneNumber = "010-5555-5555";

        assertThat(memberDataCheck.getLoginID()).isEqualTo(unaccepted_member_id);
        assertThat(memberDataCheck.getName()).isEqualTo(null);
        assertThat(memberDataCheck.getStudentID()).isEqualTo(null);
        assertThat(memberDataCheck.getDepartment()).isEqualTo(null);
        assertThat(memberDataCheck.getPhoneNumber()).isEqualTo(null);
    }

    @Test
    @DisplayName("테스트 04. 이미 면접시간 신청한 UNACCEPTANCE 회원이 면접시간 신청시 에러 발생 409")
    public void 이미_면접시간_신청한_UNACCEPTANCE회원_면접시간_신청_에러() {
        //given
        String accessToken = unacceptanceLogin();
        long AlreadyApplySlotId = 1;
        long applySlotId = 2;

        applyInterview(accessToken, AlreadyApplySlotId);

        //when
        ExtractableResponse<Response> response = applyInterview(accessToken, applySlotId);

        //then
        cancelApplyInterview(accessToken, AlreadyApplySlotId);
        cancelApplyInterview(accessToken, applySlotId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("테스트 05. UNACCEPTANCE이 아닌 회원 면접 신청시 에러 403")
    public void UNACCEPTANCE회원X_면접_시간_신청시_에러() {
        //given
        String accessToken = AuthAcceptanceTest.memberLogin();
        long applySlotId = 2;

        //when
        ExtractableResponse<Response> response = applyInterview(accessToken, applySlotId);

        //then
        cancelApplyInterview(accessToken, applySlotId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("테스트 06. UNACCEPTANCE 회원이 수용 인원이 넘은 면접 시간 신청시 에러 403")
    public void UNACCEPTANCE회원_수용_인원이_넘은_면접_시간_신청시_에러() {
        //given
        long applySlotId = 2;
        String alreadyApplyAccessToken1 = unacceptance1Login();
        String alreadyApplyAccessToken2 = unacceptance2Login();

        applyInterview(alreadyApplyAccessToken1, applySlotId);
        applyInterview(alreadyApplyAccessToken2, applySlotId);

        String accessToken = unacceptanceLogin();

        //when
        ExtractableResponse<Response> response = applyInterview(accessToken, applySlotId);

        //then
        cancelApplyInterview(alreadyApplyAccessToken1, applySlotId);
        cancelApplyInterview(alreadyApplyAccessToken2, applySlotId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("테스트 07. UNACCEPTANCE 회원 정상적으로 면접 취소 200")
    public void UNACCEPTANCE회원_INTERVIEW_면접_신청_취소() {
        //given
        String accessToken = unacceptanceLogin();
        long deleteSlotId = 2;

        applyInterview(accessToken, deleteSlotId);

        //when
        ExtractableResponse<Response> response = cancelApplyInterview(accessToken, deleteSlotId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getObject("mySlotId", Long.class)).isNull();
    }

    @Test
    @DisplayName("테스트 08. 해당 slotId에 신청하지 않은 UNACCEPTANCE 회원 면접 취소시 에러 발생 409")
    public void 해당_slotId_신청하지_않은_UNACCEPTANCE회원_면접_취소시_에러_발생() {
        //given
        String accessToken = unacceptanceLogin();
        long deleteSlotId = 1;

        //when
        ExtractableResponse<Response> response = cancelApplyInterview(accessToken, deleteSlotId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    @DisplayName("테스트 09. UNACCEPTANCE 회원이 아닐시 회원 면접 취소시 에러 발생 403")
    public void UNACCEPTACE회원x_면접_취소시_에러_발생() {
        //given
        String accessToken = memberLogin();
        long deleteSlotId = 1;

        //when
        ExtractableResponse<Response> response = cancelApplyInterview(accessToken, deleteSlotId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    @DisplayName("테스트 10. 임원진이 SLOT 정상적으로 등록 200")
    public void 임원진_SLOT_등록() {
        //given
        String accessToken = adminLogin();
        RegisterInterviewSlotRequest request = RegisterInterviewSlotRequest.builder()
                .date(LocalDate.now())
                .startTime(LocalTime.of(19, 00))
                .endTime(LocalTime.of(19, 15))
                .capacity(4)
                .build();

        //when
        ExtractableResponse<Response> response = postInterviewSlot(accessToken, request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<InterviewSlotsByDateResponse> data = response.body().jsonPath()
                .getList("data", InterviewSlotsByDateResponse.class);
        assertThat(data).isNotNull();
    }


    @Test
    @DisplayName("테스트 11. 임원진이 아닐시 SLOT 등록시 에러 발생 403")
    public void 임원진X_SLOT_등록_에러_발생() {
        //given
        String accessToken = memberLogin();
        RegisterInterviewSlotRequest request = RegisterInterviewSlotRequest.builder()
                .date(LocalDate.now())
                .startTime(LocalTime.of(19, 00))
                .endTime(LocalTime.of(19, 15))
                .capacity(4)
                .build();

        //when
        ExtractableResponse<Response> response = postInterviewSlot(accessToken, request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }


    @Test
    @DisplayName("테스트 13. 임원진이 SLOT 정상적으로 수정 200")
    public void 임원진_SLOT_수정() {
        //given
        String accessToken = adminLogin();
        long slotId = 13;
        UpdateInterviewSlotRequest request = UpdateInterviewSlotRequest.builder()
                .startTime(LocalTime.of(16, 00))
                .endTime(LocalTime.of(16, 15))
                .capacity(2)
                .build();

        //when
        ExtractableResponse<Response> response = updateInterviewSlot(accessToken, slotId, request);

        //then
        UpdateInterviewSlotRequest postRequest = UpdateInterviewSlotRequest.builder()
                .startTime(LocalTime.of(15, 45))
                .endTime(LocalTime.of(16, 00))
                .capacity(4)
                .build();
        updateInterviewSlot(accessToken, slotId, postRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<InterviewSlotsByDateResponse> data = response.body().jsonPath()
                .getList("data", InterviewSlotsByDateResponse.class);
        assertThat(data).isNotNull();
    }

    @Test
    @DisplayName("테스트 14. 임원진x SLOT 정상적으로 수정 403")
    public void 임원진x_SLOT_수정_에러_발생() {
        //given
        String accessToken = memberLogin();
        long slotId = 12;
        UpdateInterviewSlotRequest request = UpdateInterviewSlotRequest.builder()
                .startTime(LocalTime.of(15, 30))
                .endTime(LocalTime.of(15, 45))
                .capacity(3)
                .build();

        //when
        ExtractableResponse<Response> response = updateInterviewSlot(accessToken, slotId, request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    @DisplayName("테스트 15. 임원진이 SLOT 수정하지만 이미 신청한 사람수(ex: 3)보다 수용인원을 더 적게 신청시(ex: 2) 에러 발생 409")
    public void 임원진_SLOT_수정_BUT_이미_신청한_사람수보다_작게_설정시_에러() {
        //given
        String applyAccessToken1 = unacceptance1Login();
        String applyAccessToken2 = unacceptance2Login();
        String applyAccessToken3 = unacceptance3Login();
        long applySlotId = 13;
        applyInterview(applyAccessToken1, applySlotId);
        applyInterview(applyAccessToken2, applySlotId);
        applyInterview(applyAccessToken3, applySlotId);

        String accessToken = adminLogin();
        UpdateInterviewSlotRequest request = UpdateInterviewSlotRequest.builder()
                .startTime(LocalTime.of(16, 30))
                .endTime(LocalTime.of(16, 45))
                .capacity(2)
                .build();

        //when
        ExtractableResponse<Response> response = updateInterviewSlot(accessToken, applySlotId, request);

        //then
        cancelApplyInterview(applyAccessToken1, applySlotId);
        cancelApplyInterview(applyAccessToken2, applySlotId);
        cancelApplyInterview(applyAccessToken3, applySlotId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("테스트 17. 임원진이 정상적으로 면접 시간 삭제 200")
    public void 임원진_SLOT_개별_삭제() {
        //given
        String accessToken = adminLogin();
        long deleteSlotId = 14;

        //when
        ExtractableResponse<Response> response = deleteInterviewSlot(accessToken, deleteSlotId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<InterviewSlotsByDateResponse> data = response.body().jsonPath()
                .getList("data", InterviewSlotsByDateResponse.class);
        assertThat(data).isNotNull();
    }

    @Test
    @DisplayName("테스트 18. 임원진이 회원이 신청한 면접시간 정상적으로 면접 시간 삭제 200")
    public void 임원진_회원이_신청한_SLOT_개별_삭제() {
        //given
        long deleteSlotId = 15;
        String applyAccessToken = unacceptanceLogin();
        applyInterview(applyAccessToken, deleteSlotId);
        String accessToken = adminLogin();

        //when
        ExtractableResponse<Response> response = deleteInterviewSlot(accessToken, deleteSlotId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<InterviewSlotsByDateResponse> data = response.body().jsonPath()
                .getList("data", InterviewSlotsByDateResponse.class);
        assertThat(data).isNotNull();
    }

    @Test
    @DisplayName("테스트 19. 임원진이 아닌 회원 개별 SLOT 삭제시 에러 발생 403")
    public void 임원진X_SLOT_개별_삭제_에러() {
        //given
        String accessToken = memberLogin();
        long deleteSlotId = 10000;

        //when
        ExtractableResponse<Response> response = deleteInterviewSlot(accessToken, deleteSlotId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("테스트 20. 임원진이 아닌 회원 개별 SLOT 삭제시 에러 발생 403")
    public void 임원진_없는_SLOTID_개별_삭제_에러() {
        //given
        String accessToken = adminLogin();
        long deleteSlotId = 10000;

        //when
        ExtractableResponse<Response> response = deleteInterviewSlot(accessToken, deleteSlotId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("테스트 21. 임원진 회원이 SLOT 정상적으로 전체 삭제")
    public void 임원진_SLOT_전체_삭제() {
        //given
        String accessToken = adminLogin();

        //when
        ExtractableResponse<Response> response = deleteAllInterviewSlots(accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<InterviewSlotsByDateResponse> data = response.body().jsonPath()
                .getList("data", InterviewSlotsByDateResponse.class);
        assertThat(data).isEmpty();
    }

    @Test
    @DisplayName("테스트 22. 임원진x 회원이 SLOT 전체 삭제시 에러 발생")
    public void 임원진_SLOT_전체_삭제시_에러_발생() {
        //given
        String accessToken = memberLogin();

        //when
        ExtractableResponse<Response> response = deleteAllInterviewSlots(accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private ExtractableResponse<Response> getInterviewSlots(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/interview/slots")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> applyInterview(String accessToken, long slotId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("interview/application/{slotId}", slotId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> cancelApplyInterview(String accessToken, long deleteSlotId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/interview/application/{slotId}", deleteSlotId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> postInterviewSlot(String accessToken, RegisterInterviewSlotRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/interview/slots")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> updateInterviewSlot(String accessToken, long slotId, UpdateInterviewSlotRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/interview/slots/{slotId}", slotId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteInterviewSlot(String accessToken, long slotId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/interview/slots/{slotId}", slotId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteAllInterviewSlots(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/interview/slots")
                .then().log().all()
                .extract();
    }

}
