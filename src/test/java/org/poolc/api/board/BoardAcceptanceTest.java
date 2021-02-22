package org.poolc.api.board;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.board.dto.BoardResponse;
import org.poolc.api.board.dto.BoardsResponse;
import org.poolc.api.board.dto.RegisterBoardRequest;
import org.poolc.api.board.dto.UpdateBoardRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles({"boardTest", "test"})
public class BoardAcceptanceTest extends AcceptanceTest {


    @Test
    void 게시판생성() {
        String accessToken = 임원진로그인();

        ExtractableResponse<Response> response = createBoardRequest(accessToken, "board", "/board", "read", "write");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
    }


    @Test
    void 비인가자게시판생성() {
        String accessToken = 비임원진로그인();

        ExtractableResponse<Response> response = createBoardRequest(accessToken, "board", "/board", "read", "write");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 특정게시판조회() {
        String accessToken = 비임원진로그인();

        ExtractableResponse<Response> response = getBoardRequest(accessToken, "공지사항");
        BoardResponse responseBody = response.as(BoardResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getName().equals("공지사항"));
    }

    @Test
    void 없는특정게시판조회() {
        String accessToken = 비임원진로그인();

        ExtractableResponse<Response> response = getBoardRequest(accessToken, "없는게시판");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 전체게시판조회() {
        String accessToken = 비임원진로그인();


        ExtractableResponse<Response> response = getBoardsRequest(accessToken);
        BoardsResponse requestBody = response.as(BoardsResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(requestBody.getData()).hasSize(8);
    }

    @Test
    void 임원게시판삭제() {
        String accessToken = 임원진로그인();

        ExtractableResponse<Response> response = deleteBoard(accessToken, "삭제할게시판");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> 확인Response = getBoardRequest(accessToken, "삭제할게시판");
        assertThat(확인Response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 비임원게시판삭제() {
        String accessToken = 비임원진로그인();

        ExtractableResponse<Response> response = deleteBoard(accessToken, "삭제할게시판");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }


    @Test
    void 없는게시판삭제() {
        String accessToken = 임원진로그인();

        ExtractableResponse<Response> response = deleteBoard(accessToken, "없는게시판");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 임원진게시판수정() {
        String accessToken = 임원진로그인();

        UpdateBoardRequest updateBoardRequest = new UpdateBoardRequest("updateBoard2", "/updateBoard2", "true", "true");
        ExtractableResponse<Response> response = updateBoard(accessToken, "updateBoard", updateBoardRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> 확인Response = getBoardRequest(accessToken, "updateBoard2");
        BoardResponse requestBody = 확인Response.as(BoardResponse.class);
        assertThat(requestBody.getReadPermission()).isEqualTo("true");
    }

    @Test
    void 비임원진게시판수정() {
        String accessToken = 비임원진로그인();

        UpdateBoardRequest updateBoardRequest = new UpdateBoardRequest("updateBoard", "/updateBoard", "true", "true");
        ExtractableResponse<Response> response = updateBoard(accessToken, "updateBoard", updateBoardRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 없는게시판수정() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        UpdateBoardRequest updateBoardRequest = new UpdateBoardRequest("updateBoard", "/updateBoard", "true", "true");
        ExtractableResponse<Response> response = updateBoard(accessToken, "없는게시판", updateBoardRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }


    private String 임원진로그인() {
        return loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    private String 비임원진로그인() {
        return loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
    }

    public static ExtractableResponse<Response> createBoardRequest(String accessToken, String name, String URLPath, String readPermission, String writePermission) {
        RegisterBoardRequest request = new RegisterBoardRequest(name, URLPath, readPermission, writePermission);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/board")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getBoardRequest(String accessToken, String boardName) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/board/{boardName}", boardName)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getBoardsRequest(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/board")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateBoard(String accessToken, String boardName, UpdateBoardRequest updateBoardRequest) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(updateBoardRequest)
                .when().put("/board/{boardName}", boardName)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteBoard(String accessToken, String boardName) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/board/{boardName}", boardName)
                .then().log().all()
                .extract();
    }

}
