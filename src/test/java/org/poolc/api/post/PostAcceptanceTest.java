package org.poolc.api.post;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.dto.RegisterPostRequest;
import org.poolc.api.post.dto.UpdatePostRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles({"boardTest", "postTest", "memberTest"})
public class PostAcceptanceTest extends AcceptanceTest {
    private Long noticePostId = 1L;
    private Long freePostId = 2L;
    private Long adminPostId = 3L;
    private Long writerWillDeletePostId = 4L;
    private Long noWriterWillDeletePostId = 5L;
    private Long adminWillDeletePostId = 6L;
    private Long willUpdatePostId = 7L;

    private Long freeBoardId = 2L;
    private Long adminBoardId = 6L;

    private String noticeUrlPath = "notice";
    private String freeUrlPath = "free";
    private String adminUrlPath = "meeting";
    private String notExistUrlPath = "notExist";
    private final int pageNum = 1, pageSize = 15;

    @Test
    void 로그인xPUBLIC게시물조회() {
        String accessToken = "";
        ExtractableResponse<Response> response = getPostRequest(accessToken, noticePostId);
        PostResponse responseBody = response.as(PostResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getTitle()).isEqualTo("test1");
    }

    @Test
    void 로그인xMEMBER게시물조회() {
        String accessToken = "";
        ExtractableResponse<Response> response = getPostRequest(accessToken, freePostId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 로그인xADMIN게시물조회() {
        String accessToken = "";
        ExtractableResponse<Response> response = getPostRequest(accessToken, adminPostId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 멤버PUBLIC게시물조회() {
        String accessToken = 작성자비임원진로그인();

        ExtractableResponse<Response> response = getPostRequest(accessToken, noticePostId);
        PostResponse responseBody = response.as(PostResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getTitle()).isEqualTo("test1");
    }

    @Test
    void 멤버MEMBER게시물조회() {
        String accessToken = 작성자비임원진로그인();

        ExtractableResponse<Response> response = getPostRequest(accessToken, freePostId);
        PostResponse responseBody = response.as(PostResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getTitle()).isEqualTo("test2");
    }

    @Test
    void 멤버ADMIN게시물조회() {
        String accessToken = 작성자비임원진로그인();

        ExtractableResponse<Response> response = getPostRequest(accessToken, adminPostId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 임원PUBLIC게시물조회() {
        String accessToken = 임원진로그인();

        ExtractableResponse<Response> response = getPostRequest(accessToken, noticePostId);
        PostResponse responseBody = response.as(PostResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getTitle()).isEqualTo("test1");
    }

    @Test
    void 임원MEMBER게시물조회() {
        String accessToken = 임원진로그인();

        ExtractableResponse<Response> response = getPostRequest(accessToken, freePostId);
        PostResponse responseBody = response.as(PostResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getTitle()).isEqualTo("test2");
    }

    @Test
    void 임원ADMIN게시물조회() {
        String accessToken = 임원진로그인();

        ExtractableResponse<Response> response = getPostRequest(accessToken, adminPostId);
        PostResponse responseBody = response.as(PostResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody.getTitle()).isEqualTo("test3");
    }

    @Test
    void 로그인xPUBLIC게시판게시물전체조회() {
        String accessToken = "", urlPath = noticeUrlPath;
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 로그인xMEMBER게시판게시물전체조회() {
        String accessToken = "", urlPath = freeUrlPath;
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void 로그인xADMIN게시판게시물전체조회() {
        String accessToken = "", urlPath = adminUrlPath;
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 멤버PUBLIC게시판게시물전체조회() {
        String accessToken = 작성자비임원진로그인(), urlPath = noticeUrlPath;
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 멤버MEMBER게시판게시물전체조회() {
        String accessToken = 작성자비임원진로그인(), urlPath = freeUrlPath;
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 멤버ADMIN게시판게시물전체조회() {
        String accessToken = 작성자비임원진로그인(), urlPath = adminUrlPath;
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 임원PUBLIC게시판게시물전체조회() {
        String accessToken = 임원진로그인(), urlPath = freeUrlPath;
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 임원MEMBER게시판게시물전체조회() {
        String accessToken = 임원진로그인(), urlPath = freeUrlPath;
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 임원ADMIN게시판게시물전체조회() {
        String accessToken = 임원진로그인(), urlPath = freeUrlPath;
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 없는게시판게시물전체조회() {
        String accessToken = 임원진로그인(), urlPath = notExistUrlPath;
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    void 로그인x게시물생성() {
        String accessToken = "";
        List<String> file_list = new ArrayList<>();
        file_list.add("https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg");
        RegisterPostRequest request = new RegisterPostRequest(freeBoardId, "testtest", "testtest", file_list);
        ExtractableResponse<Response> response = createPostRequest(accessToken, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 멤버MEMBER게시글생성() {
        String accessToken = 작성자비임원진로그인();
        List<String> file_list = new ArrayList<>();
        file_list.add("https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg");
        file_list.add("https://s.pstatic.net/shopping.phinf/20210309_6/7865a9bf-017f-4d45-a945-006cd6903f8e.jpg");

        RegisterPostRequest request = new RegisterPostRequest(freeBoardId, "testtest", "testtest", file_list);
        ExtractableResponse<Response> response = createPostRequest(accessToken, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
    }

    @Test
    void 멤버MEMBER게시글생성중복된파일() {
        String accessToken = 작성자비임원진로그인();
        List<String> file_list = new ArrayList<>();
        file_list.add("https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg");
        file_list.add("https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg");

        RegisterPostRequest request = new RegisterPostRequest(freeBoardId, "testtest", "testtest", file_list);
        ExtractableResponse<Response> response = createPostRequest(accessToken, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 멤버ADMIN게시글생성() {
        String accessToken = 작성자비임원진로그인();

        List<String> file_list = new ArrayList<>();
        file_list.add("https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg");

        RegisterPostRequest request = new RegisterPostRequest(adminBoardId, "testtest", "testtest", file_list);
        ExtractableResponse<Response> response = createPostRequest(accessToken, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 임원MEMBER게시글생성() {
        String accessToken = 임원진로그인();

        List<String> file_list = new ArrayList<>();
        file_list.add("https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg");
        RegisterPostRequest request = new RegisterPostRequest(freeBoardId, "testtest", "testtest", file_list);
        ExtractableResponse<Response> response = createPostRequest(accessToken, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
    }


    @Test
    void 임원ADMIN게시글생성() {
        String accessToken = 임원진로그인();
        List<String> file_list = new ArrayList<>();
        file_list.add("https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg");
        RegisterPostRequest request = new RegisterPostRequest(adminBoardId, "testtest", "testtest", file_list);
        ExtractableResponse<Response> response = createPostRequest(accessToken, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
    }

    @Test
    void 작성자게시글수정() {
        String accessToken = 작성자비임원진로그인();

        List<String> file_list = new ArrayList<>();
        file_list.add("https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg");
        UpdatePostRequest request = new UpdatePostRequest("update", "update", file_list);
        ExtractableResponse<Response> response = updatePost(accessToken, willUpdatePostId, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> 확인용Response = getPostRequest(accessToken, willUpdatePostId);
        PostResponse responseBody = 확인용Response.as(PostResponse.class);

        assertThat(responseBody.getTitle()).isEqualTo("update");
    }

    @Test
    void 작성자x게시글수정() {
        String accessToken = 임원진로그인();

        List<String> file_list = new ArrayList<>();
        file_list.add("https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg");

        UpdatePostRequest request = new UpdatePostRequest("update", "update", file_list);
        ExtractableResponse<Response> response = updatePost(accessToken, willUpdatePostId, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 작성자게시글삭제() {
        String accessToken = 작성자비임원진로그인();

        ExtractableResponse<Response> response = deletePost(accessToken, writerWillDeletePostId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 작성자x게시글삭제() {
        String accessToken = 작성자x로그인();

        ExtractableResponse<Response> response = deletePost(accessToken, noWriterWillDeletePostId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 임원게시글삭제() {
        String accessToken = 임원진로그인();

        ExtractableResponse<Response> response = deletePost(accessToken, adminWillDeletePostId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 페이지네이션기능구현() {
        String accessToken = 임원진로그인(), urlPath = "pagination";
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, pageNum);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("data").size()).isEqualTo(pageSize);
    }

    @Test
    void 페이지네이션기능구현2() {
        String accessToken = 임원진로그인(), urlPath = "pagination";
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, 2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("data").size()).isEqualTo(pageSize);
    }

    @Test
    void 페이지네이션기능구현3() {
        String accessToken = 임원진로그인(), urlPath = "pagination";
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, 3);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("data").size()).isEqualTo(pageSize);
    }

    @Test
    void 페이지네이션기능구현4() {
        String accessToken = 임원진로그인(), urlPath = "pagination";
        ExtractableResponse<Response> response = getPostsByUrlPath(accessToken, urlPath, 4);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("data").size()).isEqualTo(1);
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


    public static ExtractableResponse<Response> getPostRequest(String accessToken, Long postId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/post/{postId}", postId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getPostsByUrlPath(String accessToken, String urlPath, int page) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/post/board/{urlPath}?page={page}", urlPath, page)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> createPostRequest(String accessToken, RegisterPostRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/post")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updatePost(String accessToken, Long postId, UpdatePostRequest updatePostRequest) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(updatePostRequest)
                .when().put("/post/{postId}", postId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deletePost(String accessToken, Long postId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/post/{postId}", postId)
                .then().log().all()
                .extract();
    }
}
