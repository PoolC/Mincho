package org.poolc.api.comment;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.comment.dto.RegisterCommentRequest;
import org.poolc.api.comment.dto.UpdateCommentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles({"postTest", "boardTest"})
public class CommentAcceptanceTest extends AcceptanceTest {
    private Long noticePostId = 9L;

    private Long updateCommentId = 17L;
    private Long writerWillDeleteCommentId = 19L;
    private Long willNotDeleteCommentId = 21L;

    @Test
    void 댓글생성() {
        String accessToken = 임원진로그인();
        RegisterCommentRequest registerCommentRequest = new RegisterCommentRequest(noticePostId, "test");
        ExtractableResponse<Response> response = createCommentRequest(accessToken, registerCommentRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
    }

    @Test
    void 작성자댓글수정() {
        String accessToken = 작성자비임원진로그인();
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("update");
        ExtractableResponse<Response> response = updateCommentRequest(accessToken, updateCommentId, updateCommentRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 작성자x댓글수정() {
        String accessToken = 임원진로그인();
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("update");
        ExtractableResponse<Response> response = updateCommentRequest(accessToken, updateCommentId, updateCommentRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 작성자댓글삭제() {
        String accessToken = 작성자비임원진로그인();
        ExtractableResponse<Response> response = deleteComment(accessToken, writerWillDeleteCommentId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void 작성자x댓글삭제() {
        String accessToken = 작성자x로그인();
        ExtractableResponse<Response> response = deleteComment(accessToken, willNotDeleteCommentId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 임원댓글삭제() {
        String accessToken = 임원진로그인();
        ExtractableResponse<Response> response = deleteComment(accessToken, willNotDeleteCommentId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> createCommentRequest(String accessToken, RegisterCommentRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/comment")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateCommentRequest(String accessToken, Long commentId, UpdateCommentRequest updateCommentRequest) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(updateCommentRequest)
                .when().put("/comment/{commentId}", commentId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteComment(String accessToken, Long commentId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/comment/{commentId}", commentId)
                .then().log().all()
                .extract();
    }

    private String 임원진로그인() {
        return loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    private String 작성자비임원진로그인() {
        return loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    private String 작성자x로그인() {
        return loginRequest("UPDATE_MEMBER_ID", "UPDATE_MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

}
