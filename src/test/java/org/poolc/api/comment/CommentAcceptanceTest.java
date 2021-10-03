package org.poolc.api.comment;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.comment.dto.RegisterCommentRequest;
import org.poolc.api.comment.dto.UpdateCommentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles({"postTest", "boardTest", "memberTest"})
public class CommentAcceptanceTest extends AcceptanceTest {
    private final Long NOTICE_POST_ID = 7L;
    private final Long EXIST_COMMENT_ID = 1L;
    private final Long WILL_NOT_DELETE_COMMENT_ID = 21L;

    @Test
    void 댓글생성() {
        String accessToken = 임원진로그인();
        ExtractableResponse<Response> response = createCommentRequest(accessToken, new RegisterCommentRequest(NOTICE_POST_ID, "test"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 작성자댓글수정() {
        String accessToken = 작성자비임원진로그인();
        ExtractableResponse<Response> commentResponse = createCommentRequest(accessToken, new RegisterCommentRequest(NOTICE_POST_ID, "test"));
        CommentResponse comment = commentResponse.as(CommentResponse.class);

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("update");
        ExtractableResponse<Response> response = updateCommentRequest(accessToken, comment.getCommentId(), updateCommentRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 작성자x댓글수정() {
        String memberAccessToken = 작성자비임원진로그인();
        String accessToken = 임원진로그인();
        ExtractableResponse<Response> commentResponse = createCommentRequest(memberAccessToken, new RegisterCommentRequest(NOTICE_POST_ID, "test"));
        CommentResponse comment = commentResponse.as(CommentResponse.class);

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("update");
        ExtractableResponse<Response> response = updateCommentRequest(accessToken, comment.getCommentId(), updateCommentRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 작성자댓글삭제() {
        String accessToken = 작성자비임원진로그인();
        ExtractableResponse<Response> commentResponse = createCommentRequest(accessToken, new RegisterCommentRequest(NOTICE_POST_ID, "test"));
        CommentResponse comment = commentResponse.as(CommentResponse.class);

        ExtractableResponse<Response> response = deleteComment(accessToken, comment.getCommentId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 작성자x댓글삭제() {
        String accessToken = 작성자x로그인();
        ExtractableResponse<Response> response = deleteComment(accessToken, EXIST_COMMENT_ID);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 임원댓글삭제() {
        String accessToken = 임원진로그인();
        String memberAccessToken = 작성자비임원진로그인();
        ExtractableResponse<Response> commentResponse = createCommentRequest(memberAccessToken, new RegisterCommentRequest(NOTICE_POST_ID, "test"));
        CommentResponse comment = commentResponse.as(CommentResponse.class);

        ExtractableResponse<Response> response = deleteComment(accessToken, comment.getCommentId());

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
